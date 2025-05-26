package com.example.Project.service.impl;

import com.example.Project.controller.CartController;
import com.example.Project.dto.OrderDTO;
import com.example.Project.dto.OrderDetailDTO;
import com.example.Project.entity.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductRepository productRepository;

    @Override
    public OrderDTO getCurrentCart(Long userId) {
        // Lấy giỏ hàng chưa thanh toán và chưa bị xóa mềm của user
        List<Order> carts = orderRepository.findByCustomerIdAndIsActiveFalseAndIsDeleteFalse(userId);
        Order cart;
        if (carts.isEmpty()) {
            cart = createEmptyCartForUser(userId);
        } else {
            cart = carts.get(0); // Giả sử chỉ 1 giỏ hàng hiện tại
        }
        return orderMapper.toDto(cart);
    }

    private Order createEmptyCartForUser(Long userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Order cart = new Order();
        cart.setCustomer(customer);
        cart.setIsActive(false);
        cart.setIsDelete(false);
        cart.setOrdersDate(LocalDateTime.now());

        // CẦN THÊM PHẦN TẠO ID ĐƠN HÀNG NGẪU NHIÊN HOẶC THEO QUY TẮC RIÊNG
        cart.setIdOrders(generateOrderCode());

        orderRepository.save(cart);
        return cart;
    }

    private String generateOrderCode() {
        long timestamp = System.currentTimeMillis();
        int randomNum = (int)(Math.random() * 9000) + 1000; // số ngẫu nhiên 4 chữ số từ 1000 đến 9999
        return "ORD-" + timestamp + "-" + randomNum;
    }



    @Override
    public OrderDetailDTO addToCart(Long userId, OrderDetailDTO orderDetailDTO) {
        Order cart = getCurrentCartEntity(userId);

        OrderDetail existingItem = orderDetailRepository.findByOrderIdAndProductId(cart.getId(), orderDetailDTO.productId());
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + orderDetailDTO.quantity());
            existingItem.setTotal(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            orderDetailRepository.save(existingItem);
            return orderDetailMapper.toDto(existingItem);
        }

        OrderDetail detail = orderDetailMapper.toEntity(orderDetailDTO);

        // Thêm phần gán giá sản phẩm tại đây:
        if (detail.getPrice() == null) {
            // Lấy giá sản phẩm từ repository hoặc DTO (tuỳ bạn thiết kế)
            BigDecimal productPrice = productRepository.findById(orderDetailDTO.productId())
                    .map(Product::getPrice)
                    .orElse(BigDecimal.ZERO);
            detail.setPrice(productPrice);
        }

        detail.setOrder(cart);
        detail.setTotal(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        orderDetailRepository.save(detail);
        return orderDetailMapper.toDto(detail);
    }


    private Order getCurrentCartEntity(Long userId) {
        List<Order> carts = orderRepository.findByCustomerIdAndIsActiveFalseAndIsDeleteFalse(userId);
        if (carts.isEmpty()) {
            return createEmptyCartForUser(userId);
        }
        return carts.get(0);
    }

    @Override
    public OrderDetailDTO updateCartItem(Long userId, Long itemId, OrderDetailDTO orderDetailDTO) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        checkAccess(detail.getOrder(), userId);

        detail.setQuantity(orderDetailDTO.quantity());
        detail.setPrice(orderDetailDTO.price());
        detail.setTotal(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        orderDetailRepository.save(detail);
        return orderDetailMapper.toDto(detail);
    }

    @Override
    public OrderDetailDTO updateItemQuantity(Long userId, Long itemId, Integer quantity) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        checkAccess(detail.getOrder(), userId);

        detail.setQuantity(quantity);
        detail.setTotal(detail.getPrice().multiply(BigDecimal.valueOf(quantity)));
        orderDetailRepository.save(detail);
        return orderDetailMapper.toDto(detail);
    }

    @Override
    public void removeFromCart(Long userId, Long itemId) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        checkAccess(detail.getOrder(), userId);

        orderDetailRepository.delete(detail);
    }

    @Override
    public void clearCart(Long userId) {
        Order cart = getCurrentCartEntity(userId);
        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        orderDetailRepository.deleteAll(details);
    }

    @Override
    public Integer getCartItemCount(Long userId) {
//        Order cart = getCurrentCartEntity(userId);
//        int x = orderDetailRepository.countByOrderId(cart.getId());
//        return x;
        OrderDTO cart = getCurrentCart(userId);
        if (cart.getOrderDetails() == null) {
            return 0;
        }
        // Sum tất cả quantity
        return cart.getOrderDetails()
                .stream()
                .mapToInt(od -> od.quantity())  // record OrderDetailDTO.quantity()
                .sum();
    }

    @Override
    public BigDecimal getCartTotal(Long userId) {
        Order cart = getCurrentCartEntity(userId);
        if (cart == null || cart.getId() == null) {
            return BigDecimal.ZERO;
        }

        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());

        BigDecimal total = details.stream()
                .map(OrderDetail::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (cart.getCoupon() != null) {
            BigDecimal discount = cart.getCoupon().getDiscountAmount();
            total = total.subtract(discount).max(BigDecimal.ZERO);
        }

        return total;
    }


    @Override
    public OrderDTO applyCoupon(Long userId, String couponCode) {
        Order cart = getCurrentCartEntity(userId);

        Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(couponCode)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found or inactive"));

        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate().isAfter(now) || coupon.getEndDate().isBefore(now)) {
            throw new IllegalArgumentException("Coupon is expired or not yet valid");
        }

        cart.setCoupon(coupon);
        orderRepository.save(cart);
        return orderMapper.toDto(cart);
    }

    @Override
    public OrderDTO removeCoupon(Long userId) {
        Order cart = getCurrentCartEntity(userId);
        cart.setCoupon(null);
        orderRepository.save(cart);
        return orderMapper.toDto(cart);
    }

    @Override
    public List<String> validateCart(Long userId) {
        Order cart = getCurrentCartEntity(userId);
        List<OrderDetail> details = orderDetailRepository.findByOrderId(cart.getId());
        List<String> errors = new ArrayList<>();

        for (OrderDetail detail : details) {
            if (detail.getQuantity() > detail.getProduct().getQuantity()) {
                errors.add("Sản phẩm " + detail.getProduct().getName() + " không đủ số lượng");
            }
        }
        return errors;
    }

    @Override
    public OrderDTO checkout(Long userId, CartController.CheckoutRequest checkoutRequest) {
        Order cart = getCurrentCartEntity(userId);

        cart.setNameReceiver(checkoutRequest.getNameReceiver());
        cart.setAddress(checkoutRequest.getAddress());
        cart.setEmail(checkoutRequest.getEmail());
        cart.setPhone(checkoutRequest.getPhone());
        cart.setNotes(checkoutRequest.getNotes());

        // Chỉ set payment method nếu client truyền paymentMethodId
        if (checkoutRequest.getPaymentMethodId() != null) {
            var paymentMethod = paymentMethodRepository.findById(checkoutRequest.getPaymentMethodId())
                    .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));
            cart.setPaymentMethod(paymentMethod);
        }

        // Chỉ set transport method nếu client truyền transportMethodId
        if (checkoutRequest.getTransportMethodId() != null) {
            var transportMethod = transportMethodRepository.findById(checkoutRequest.getTransportMethodId())
                    .orElseThrow(() -> new EntityNotFoundException("Transport method not found"));
            cart.setTransportMethod(transportMethod);
        }

        // Kích hoạt đơn
        cart.setIsActive(true);
        orderRepository.save(cart);

        return orderMapper.toDto(cart);
    }


    @Override
    public void saveForLater(Long userId, Long itemId) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        checkAccess(detail.getOrder(), userId);

        detail.setSavedForLater(true);
        orderDetailRepository.save(detail);
    }

    @Override
    public OrderDetailDTO moveToCart(Long userId, Long itemId) {
        OrderDetail detail = orderDetailRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        checkAccess(detail.getOrder(), userId);

        detail.setSavedForLater(false);
        orderDetailRepository.save(detail);
        return orderDetailMapper.toDto(detail);
    }

    private void checkAccess(Order order, Long userId) {
        if (!order.getCustomer().getId().equals(userId)) {
            throw new SecurityException("Access denied");
        }
    }
}
