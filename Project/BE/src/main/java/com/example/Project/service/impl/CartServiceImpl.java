package com.example.Project.service.impl;

import com.example.Project.controller.CartController;
import com.example.Project.dto.OrderDTO;
import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.dto.ShippingOptionDTO;
import com.example.Project.entity.*;
import com.example.Project.entity.Coupon.CouponType;
import com.example.Project.mapper.OrderDetailMapper;
import com.example.Project.mapper.OrderMapper;
import com.example.Project.repository.*;
import com.example.Project.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransportMethodRepository transportMethodRepository;
    private final ProductRepository productRepository;

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;

    // ================================ Public API =================================

    private OrderDTO emptyCartDto() {
        OrderDTO dto = new OrderDTO();
        dto.setTotalItems(0);
        dto.setMerchandiseSubtotal(BigDecimal.ZERO);
        dto.setShippingFee(BigDecimal.ZERO);
        dto.setDiscountAmount(BigDecimal.ZERO);
        dto.setShippingDiscountAmount(BigDecimal.ZERO);
        dto.setTotalMoney(BigDecimal.ZERO);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getCurrentCart(Long userId) {
        Order cart = findDraftCart(userId);
        return cart != null ? toDtoWithComputed(cart) : emptyCartDto();
    }

    @Override
    public OrderDetailDTO addToCart(Long userId, OrderDetailDTO req) {
        Order cart = getOrCreateDraftCart(userId);

        // nếu đã có item -> cộng dồn
        OrderDetail existing = orderDetailRepository.findByOrderIdAndProductId(cart.getId(), req.productId());
        if (existing != null) {
            int newQty = (existing.getQuantity() == null ? 0 : existing.getQuantity()) + req.quantity();
            existing.setQuantity(newQty);
            existing.setTotal(safe(existing.getPrice()).multiply(BigDecimal.valueOf(newQty)));
            orderDetailRepository.save(existing);

            fillMoneyFields(cart, null);
            orderRepository.save(cart);
            return orderDetailMapper.toDto(existing);
        }

        // tạo item mới
        OrderDetail detail = orderDetailMapper.toEntity(req);
        if (detail.getPrice() == null) {
            BigDecimal price = productRepository.findById(req.productId())
                    .map(Product::getPrice).orElse(BigDecimal.ZERO);
            detail.setPrice(price);
        }
        detail.setOrder(cart);
        detail.setTotal(safe(detail.getPrice()).multiply(BigDecimal.valueOf(detail.getQuantity())));
        orderDetailRepository.save(detail);

        fillMoneyFields(cart, null);
        orderRepository.save(cart);
        return orderDetailMapper.toDto(detail);
    }

    @Override
    public OrderDetailDTO updateCartItem(Long userId, Long itemId, OrderDetailDTO req) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        checkAccess(detail.getOrder(), userId);

        detail.setQuantity(req.quantity());
        detail.setPrice(req.price());
        detail.setTotal(safe(detail.getPrice()).multiply(BigDecimal.valueOf(detail.getQuantity())));
        orderDetailRepository.save(detail);

        Order cart = detail.getOrder();
        fillMoneyFields(cart, null);
        orderRepository.save(cart);
        return orderDetailMapper.toDto(detail);
    }

    @Override
    public OrderDetailDTO updateItemQuantity(Long userId, Long itemId, Integer quantity) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        checkAccess(detail.getOrder(), userId);

        int qty = quantity == null ? 0 : quantity;
        detail.setQuantity(qty);
        detail.setTotal(safe(detail.getPrice()).multiply(BigDecimal.valueOf(qty)));
        orderDetailRepository.save(detail);

        Order cart = detail.getOrder();
        fillMoneyFields(cart, null);
        orderRepository.save(cart);
        return orderDetailMapper.toDto(detail);
    }

    @Override
    public void removeFromCart(Long userId, Long itemId) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        Order cart = detail.getOrder();
        checkAccess(cart, userId);
        orderDetailRepository.delete(detail);

        fillMoneyFields(cart, null);
        orderRepository.save(cart);
    }

    @Override
    public void clearCart(Long userId) {
        Order cart = findDraftCart(userId);
        if (cart == null) return;

        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        orderDetailRepository.deleteAll(details);

        cart.setDiscountCoupon(null);
        cart.setShippingCoupon(null);

        fillMoneyFields(cart, null);
        orderRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getCartItemCount(Long userId) {
        Order cart = findDraftCart(userId);
        if (cart == null) return 0;
        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        return details.stream()
                .filter(od -> !Boolean.TRUE.equals(od.getSavedForLater()))
                .mapToInt(od -> od.getQuantity() == null ? 0 : od.getQuantity())
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(Long userId) {
        Order cart = findDraftCart(userId);
        if (cart == null) return BigDecimal.ZERO;

        // Tính “khô” để không mutate entity (tránh flush)
        BigDecimal sub  = computeSubtotal(cart);
        BigDecimal ship = computeShippingFee(cart, null);
        BigDecimal discItems = computeMerchDiscount(cart.getDiscountCoupon(), sub);
        if (discItems.compareTo(sub) > 0) discItems = sub;
        BigDecimal discShip = computeShippingDiscount(cart.getShippingCoupon(), ship);
        if (discShip.compareTo(ship) > 0) discShip = ship;

        BigDecimal finalSubtotal = sub.subtract(discItems).max(BigDecimal.ZERO);
        BigDecimal finalShip     = ship.subtract(discShip).max(BigDecimal.ZERO);
        return finalSubtotal.add(finalShip).max(BigDecimal.ZERO);
    }

    @Override
    public OrderDTO applyCoupon(Long userId, String couponCode) {
        Order cart = getOrCreateDraftCart(userId);

        Coupon coupon = couponRepository.findByCodeIgnoreCase(couponCode)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));

        if (Boolean.TRUE.equals(coupon.getIsDelete()))  throw new IllegalArgumentException("Coupon đã bị xoá");
        if (!Boolean.TRUE.equals(coupon.getIsActive())) throw new IllegalArgumentException("Coupon không hoạt động");

        // hiệu lực theo UTC
        LocalDateTime nowUtc = LocalDateTime.now(ZoneOffset.UTC);
        if (coupon.getStartAtUtc() == null || coupon.getEndAtUtc() == null
                || nowUtc.isBefore(coupon.getStartAtUtc())
                || nowUtc.isAfter(coupon.getEndAtUtc())) {
            throw new IllegalArgumentException("Coupon chưa đến hạn hoặc đã hết hạn");
        }

        if (isShippingCoupon(coupon)) {
            cart.setShippingCoupon(pickBetterShippingCoupon(cart, coupon));
        } else if (isDiscountCoupon(coupon)) {
            cart.setDiscountCoupon(pickBetterDiscountCoupon(cart, coupon));
        } else {
            throw new IllegalArgumentException("Loại coupon không hợp lệ");
        }

        fillMoneyFields(cart, null);
        orderRepository.save(cart);
        return toDtoFromEntity(cart);
    }

    @Override
    public OrderDTO removeCoupon(Long userId) {
        Order cart = findDraftCart(userId);
        if (cart == null) return emptyCartDto();

        cart.setDiscountCoupon(null);
        cart.setShippingCoupon(null);

        fillMoneyFields(cart, null);
        orderRepository.save(cart);
        return toDtoFromEntity(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> validateCart(Long userId) {
        Order cart = findDraftCart(userId);
        if (cart == null) return List.of(); // không tạo cart khi validate trống

        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        List<String> errors = new ArrayList<>();
        for (OrderDetail d : details) {
            if (d.getProduct() != null && d.getQuantity() > d.getProduct().getQuantity()) {
                errors.add("Sản phẩm " + d.getProduct().getName() + " không đủ số lượng");
            }
        }
        return errors;
    }

    @Override
    public OrderDTO checkout(Long userId, CartController.CheckoutRequest req) {
        Order cart = getOrCreateDraftCart(userId);

        cart.setNameReceiver(req.getNameReceiver());
        cart.setAddress(req.getAddress());
        cart.setEmail(req.getEmail());
        cart.setPhone(req.getPhone());
        cart.setNotes(req.getNotes());

        if (req.getPaymentMethodId() != null) {
            PaymentMethod pm = paymentMethodRepository.findById(req.getPaymentMethodId())
                    .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));
            cart.setPaymentMethod(pm);
        }

        if (req.getTransportMethodId() != null) {
            TransportMethod tm = transportMethodRepository.findById(req.getTransportMethodId())
                    .orElseThrow(() -> new EntityNotFoundException("Transport method not found"));
            cart.setTransportMethod(tm);
        }

        // validate hỏa tốc > 15km
        if (getShipType(cart) == ShipType.EXPRESS && req.getDistanceKm() != null && req.getDistanceKm() > 15d) {
            throw new IllegalArgumentException("Không hỗ trợ hỏa tốc ngoài 15 km");
        }

        cart.setIsActive(true);

        // chốt toàn bộ tiền (dựa trên distanceKm nếu có)
        fillMoneyFields(cart, req.getDistanceKm());
        orderRepository.save(cart);
        return toDtoFromEntity(cart);
    }

    private Order findDraftCart(Long userId) {
        List<Order> carts = orderRepository
                .findByCustomerIdAndIsActiveFalseAndIsDeleteFalse(userId);
        return carts.isEmpty() ? null : carts.get(0);
    }

    @Override
    public void saveForLater(Long userId, Long itemId) {
        OrderDetail d = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        checkAccess(d.getOrder(), userId);
        if (!Boolean.TRUE.equals(d.getSavedForLater())) {
            d.setSavedForLater(true);
            orderDetailRepository.save(d);
        }
        fillMoneyFields(d.getOrder(), null);
        orderRepository.save(d.getOrder());
    }

    @Override
    public OrderDetailDTO moveToCart(Long userId, Long itemId) {
        OrderDetail d = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        checkAccess(d.getOrder(), userId);

        d.setSavedForLater(false);
        if (d.getQuantity() == null || d.getQuantity() <= 0) d.setQuantity(1);
        d.setTotal(safe(d.getPrice()).multiply(BigDecimal.valueOf(d.getQuantity())));
        orderDetailRepository.save(d);

        fillMoneyFields(d.getOrder(), null);
        orderRepository.save(d.getOrder());
        return orderDetailMapper.toDto(d);
    }

    // ================================ Core money =================================

    /** Tính & ghi tất cả tiền vào entity Order để dùng thống nhất (VNPay, FE, admin). */
    private void fillMoneyFields(Order cart, Double distanceKmOpt) {
        BigDecimal sub  = computeSubtotal(cart);
        BigDecimal ship = computeShippingFee(cart, distanceKmOpt);

        BigDecimal discItems = computeMerchDiscount(cart.getDiscountCoupon(), sub);
        if (discItems.compareTo(sub) > 0) discItems = sub;

        BigDecimal discShip = computeShippingDiscount(cart.getShippingCoupon(), ship);
        if (discShip.compareTo(ship) > 0) discShip = ship;

        BigDecimal finalSubtotal = sub.subtract(discItems).max(BigDecimal.ZERO);
        BigDecimal finalShip     = ship.subtract(discShip).max(BigDecimal.ZERO);
        BigDecimal total         = finalSubtotal.add(finalShip).max(BigDecimal.ZERO);

        cart.setSubTotal(sub);
        cart.setShippingFee(ship);
        cart.setDiscountAmount(discItems);
        cart.setShippingDiscountAmount(discShip);
        cart.setTotalMoney(total);
    }

    private BigDecimal computeSubtotal(Order cart) {
        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        return details.stream()
                .filter(od -> !Boolean.TRUE.equals(od.getSavedForLater()))
                .map(od -> {
                    BigDecimal price = safe(od.getPrice());
                    int qty = od.getQuantity() == null ? 0 : od.getQuantity();
                    return (od.getTotal() != null) ? od.getTotal() : price.multiply(BigDecimal.valueOf(qty));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private enum ShipType { UNSET, STANDARD, EXPRESS }

    private ShipType getShipType(Order cart) {
        try {
            TransportMethod tm = cart.getTransportMethod();
            if (tm == null) return ShipType.UNSET;
            String name = tm.getName() == null ? "" : tm.getName().toUpperCase(Locale.ROOT);
            if (name.contains("EXPRESS") || name.contains("HỎA TỐC") || name.contains("HOA TOC")) {
                return ShipType.EXPRESS;
            }
            return ShipType.STANDARD;
        } catch (Exception ignored) {}
        return ShipType.UNSET;
    }

    /**
     * STANDARD: 15.000đ cố định (không giới hạn km).
     * EXPRESS:  ≤ 15km. 5.000đ cho 2km đầu; từ km thứ 3 +4.000đ/km (làm tròn lên).
     * UNSET (chưa chọn): 0đ.
     * - Khi distanceKmOpt == null: với EXPRESS trả 0 (chưa biết km).
     */
    private BigDecimal computeShippingFee(Order cart, Double distanceKmOpt) {
        ShipType type = getShipType(cart);

        if (type == ShipType.UNSET) {
            return BigDecimal.ZERO; // chưa chọn phương thức
        }
        if (type == ShipType.STANDARD) {
            return BigDecimal.valueOf(15_000L);
        }

        // EXPRESS
        if (distanceKmOpt == null) {
            return BigDecimal.ZERO; // xem giỏ: chưa biết km → tạm 0
        }

        double km = Math.max(0d, distanceKmOpt);
        if (km > 15d) {
            throw new IllegalArgumentException("Không hỗ trợ hỏa tốc ngoài 15 km");
        }

        long fee = 5_000L; // 2 km đầu
        if (km > 2d) {
            long extraKm = (long) Math.ceil(km - 2d); // làm tròn lên từng km
            fee += extraKm * 4_000L;
        }
        return BigDecimal.valueOf(fee);
    }

    // ================================ Coupons ====================================

    private boolean isShippingCoupon(Coupon c) {
        return c != null && (c.getType() == CouponType.FREE_SHIPPING || c.getType() == CouponType.SHIPPING_DISCOUNT);
    }

    private boolean isDiscountCoupon(Coupon c) {
        return c != null && (c.getType() == CouponType.PERCENT || c.getType() == CouponType.FIXED || c.getType() == CouponType.CASHBACK_COINS);
    }

    private Coupon pickBetterShippingCoupon(Order cart, Coupon candidate) {
        // Không có distance → ước tính phí ship là 0 để tránh tính sai;
        BigDecimal fee = computeShippingFee(cart, null);
        Coupon current = cart.getShippingCoupon();
        if (current == null) return candidate;
        BigDecimal currSaved = computeShippingDiscount(current, fee);
        BigDecimal newSaved  = computeShippingDiscount(candidate, fee);
        return newSaved.compareTo(currSaved) > 0 ? candidate : current;
    }

    private Coupon pickBetterDiscountCoupon(Order cart, Coupon candidate) {
        BigDecimal sub = computeSubtotal(cart);
        Coupon current = cart.getDiscountCoupon();
        if (current == null) return candidate;
        BigDecimal currSaved = computeMerchDiscount(current, sub);
        BigDecimal newSaved  = computeMerchDiscount(candidate, sub);
        return newSaved.compareTo(currSaved) > 0 ? candidate : current;
    }

    private BigDecimal computeMerchDiscount(Coupon c, BigDecimal subtotal) {
        if (c == null) return BigDecimal.ZERO;
        BigDecimal max = safe(c.getMaxDiscountValue());
        BigDecimal raw = switch (c.getType()) {
            case PERCENT               -> percentage(subtotal, safe(c.getDiscountValue()));
            case FIXED, CASHBACK_COINS -> safe(c.getDiscountValue());
            default                    -> BigDecimal.ZERO;
        };
        BigDecimal capped = (max.signum() > 0) ? raw.min(max) : raw;
        return capped.min(safe(subtotal)).max(BigDecimal.ZERO);
    }

    private BigDecimal computeShippingDiscount(Coupon c, BigDecimal shippingFee) {
        if (c == null) return BigDecimal.ZERO;
        BigDecimal max = safe(c.getMaxDiscountValue());
        BigDecimal raw = switch (c.getType()) {
            case FREE_SHIPPING      -> safe(shippingFee);
            case SHIPPING_DISCOUNT  -> safe(c.getDiscountValue()).min(safe(shippingFee));
            default                 -> BigDecimal.ZERO;
        };
        BigDecimal capped = (max.signum() > 0) ? raw.min(max) : raw;
        return capped.min(safe(shippingFee)).max(BigDecimal.ZERO);
    }

    // ================================ Helpers ====================================

    private Order getOrCreateDraftCart(Long userId) {
        List<Order> carts = orderRepository.findByCustomerIdAndIsActiveFalseAndIsDeleteFalse(userId);
        if (!carts.isEmpty()) return carts.get(0);
        return createEmptyCartForUser(userId);
    }

    private Order createEmptyCartForUser(Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order cart = new Order();
        cart.setCustomer(customer);
        cart.setIsActive(false);
        cart.setIsDelete(false);
        cart.setOrdersDate(LocalDateTime.now());
        cart.setIdOrders(generateOrderCode());

        // init tiền = 0 để tránh null ở FE
        cart.setSubTotal(BigDecimal.ZERO);
        cart.setShippingFee(BigDecimal.ZERO);
        cart.setDiscountAmount(BigDecimal.ZERO);
        cart.setShippingDiscountAmount(BigDecimal.ZERO);
        cart.setTotalMoney(BigDecimal.ZERO);

        orderRepository.save(cart);
        return cart;
    }

    private void checkAccess(Order order, Long userId) {
        if (order == null || order.getCustomer() == null || !order.getCustomer().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
    }

    private String generateOrderCode() {
        long ts = System.currentTimeMillis();
        int rand = (int) (Math.random() * 9000) + 1000;
        return "ORD-" + ts + "-" + rand;
    }

    private BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        if (base == null || percent == null) return BigDecimal.ZERO;
        return base.multiply(percent).divide(BigDecimal.valueOf(100));
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private OrderDTO toDtoFromEntity(Order o) {
        OrderDTO dto = orderMapper.toDto(o);
        dto.setMerchandiseSubtotal(o.getSubTotal());
        dto.setShippingFee(o.getShippingFee());
        dto.setDiscountAmount(o.getDiscountAmount());
        dto.setShippingDiscountAmount(o.getShippingDiscountAmount());
        dto.setTotalMoney(o.getTotalMoney());
        dto.setDiscountCouponCode(o.getDiscountCoupon() != null ? o.getDiscountCoupon().getCode() : null);
        dto.setShippingCouponCode(o.getShippingCoupon() != null ? o.getShippingCoupon().getCode() : null);
        return dto;
    }

    /** Tạo DTO + tự tính tiền nhưng KHÔNG mutate entity (tránh flush). */
    private OrderDTO toDtoWithComputed(Order cart) {
        BigDecimal sub  = computeSubtotal(cart);
        BigDecimal ship = computeShippingFee(cart, null);
        BigDecimal discItems = computeMerchDiscount(cart.getDiscountCoupon(), sub);
        if (discItems.compareTo(sub) > 0) discItems = sub;
        BigDecimal discShip = computeShippingDiscount(cart.getShippingCoupon(), ship);
        if (discShip.compareTo(ship) > 0) discShip = ship;

        BigDecimal finalSubtotal = sub.subtract(discItems).max(BigDecimal.ZERO);
        BigDecimal finalShip     = ship.subtract(discShip).max(BigDecimal.ZERO);
        BigDecimal total         = finalSubtotal.add(finalShip).max(BigDecimal.ZERO);

        OrderDTO dto = orderMapper.toDto(cart);
        dto.setMerchandiseSubtotal(sub);
        dto.setShippingFee(ship);
        dto.setDiscountAmount(discItems);
        dto.setShippingDiscountAmount(discShip);
        dto.setTotalMoney(total);
        dto.setDiscountCouponCode(cart.getDiscountCoupon() != null ? cart.getDiscountCoupon().getCode() : null);
        dto.setShippingCouponCode(cart.getShippingCoupon() != null ? cart.getShippingCoupon().getCode() : null);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShippingOptionDTO> getShippingOptions(Double distanceKm) {
        // Lấy tất cả phương thức active
        List<TransportMethod> active = transportMethodRepository.findAll().stream()
                .filter(tm -> !Boolean.TRUE.equals(tm.getIsDelete()))
                .filter(tm -> Boolean.TRUE.equals(tm.getIsActive()))
                .collect(Collectors.toList());

        List<ShippingOptionDTO> out = new ArrayList<>();
        for (TransportMethod tm : active) {
            ShipType type = resolveType(tm);
            ShippingOptionDTO.ShippingOptionDTOBuilder b = ShippingOptionDTO.builder()
                    .id(tm.getId())
                    .name(tm.getName())
                    .code(type.name())
                    .supported(true)
                    .fee(null)
                    .note(null);

            if (type == ShipType.STANDARD) {
                b.fee(BigDecimal.valueOf(15_000L)); // cố định
            } else { // EXPRESS
                if (distanceKm == null) {
                    b.fee(null).note("Cần khoảng cách để tính phí");
                } else if (distanceKm > 15d) {
                    b.supported(false).note("Không hỗ trợ hỏa tốc ngoài 15 km");
                } else {
                    b.fee(calcExpressFee(distanceKm));
                }
            }
            out.add(b.build());
        }
        // Nếu không có dữ liệu method trong DB, vẫn trả 2 option mặc định
        if (out.isEmpty()) {
            out.add(ShippingOptionDTO.builder()
                    .id(null).name("Giao tiêu chuẩn").code("STANDARD")
                    .supported(true).fee(BigDecimal.valueOf(15_000L)).note(null).build());
            if (distanceKm == null) {
                out.add(ShippingOptionDTO.builder()
                        .id(null).name("Hỏa tốc (≤ 15 km)").code("EXPRESS")
                        .supported(true).fee(null).note("Cần khoảng cách để tính phí").build());
            } else if (distanceKm > 15d) {
                out.add(ShippingOptionDTO.builder()
                        .id(null).name("Hỏa tốc (≤ 15 km)").code("EXPRESS")
                        .supported(false).fee(null).note("Không hỗ trợ hỏa tốc ngoài 15 km").build());
            } else {
                out.add(ShippingOptionDTO.builder()
                        .id(null).name("Hỏa tốc (≤ 15 km)").code("EXPRESS")
                        .supported(true).fee(calcExpressFee(distanceKm)).note(null).build());
            }
        }
        return out;
    }

    private BigDecimal calcExpressFee(double km) {
        long fee = 5_000L; // 2km đầu
        if (km > 2d) {
            long extraKm = (long) Math.ceil(km - 2d); // làm tròn lên
            fee += extraKm * 4_000L;
        }
        return BigDecimal.valueOf(fee);
    }

    private ShipType resolveType(TransportMethod tm) {
        String name = tm.getName() == null ? "" : tm.getName().toUpperCase(Locale.ROOT);
        if (name.contains("EXPRESS") || name.contains("HỎA TỐC") || name.contains("HOA TOC"))
            return ShipType.EXPRESS;
        return ShipType.STANDARD;
    }
}
