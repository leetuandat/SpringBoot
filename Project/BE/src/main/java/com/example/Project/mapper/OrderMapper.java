package com.example.Project.mapper;

import com.example.Project.dto.OrderDTO;
import com.example.Project.entity.Coupon;
import com.example.Project.entity.Order;
import com.example.Project.entity.OrderDetail;
import com.example.Project.entity.TransportMethod;
// Nếu coupon của bạn ở package khác, chỉnh lại import cho đúng:
import org.mapstruct.*;
import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@Mapper(
        componentModel = "spring",
        uses = { OrderDetailMapper.class }
)
public abstract class OrderMapper {

    // ====== Entity -> DTO ======
    @Mapping(source = "idOrders",           target = "orderCode")
    @Mapping(source = "ordersDate",         target = "orderDate")
    @Mapping(source = "customer.id",        target = "customerId")
    @Mapping(source = "paymentMethod.id",   target = "paymentId")
    @Mapping(source = "transportMethod.id", target = "transportId")
    @Mapping(source = "customer.name",      target = "nameReceiver")
    @Mapping(source = "orderDetails",       target = "orderDetails")
    @Mapping(source = "totalMoney",         target = "totalMoney")
    // tên hiển thị phương thức (tuỳ field thực tế của bạn: getName()/getTitle()/getMethodName()…)
    @Mapping(target = "paymentMethodName",   expression = "java(entity.getPaymentMethod()!=null ? entity.getPaymentMethod().getName() : null)")
    @Mapping(target = "transportMethodName", expression = "java(entity.getTransportMethod()!=null ? entity.getTransportMethod().getName() : null)")
    public abstract OrderDTO toDto(Order entity);

    @AfterMapping
    protected void enrichDto(@MappingTarget OrderDTO dto, Order entity) {
        BigDecimal subtotal = computeSubtotal(entity);
        BigDecimal shipFee  = getShippingFee(entity);

        // ====== Tính giảm giá theo coupon (nếu bạn đã tách 2 loại) ======
        Coupon discountCpn = getDiscountCoupon(entity);   // lấy từ Order nếu có
        Coupon shippingCpn = getShippingCoupon(entity);   // lấy từ Order nếu có

        BigDecimal discount         = computeDiscountForDiscountCoupon(discountCpn, subtotal);
        BigDecimal shippingDiscount = computeDiscountForShippingCoupon(shippingCpn, shipFee);

        BigDecimal total = subtotal
                .subtract(discount)
                .add(shipFee.subtract(shippingDiscount).max(ZERO))
                .max(ZERO);

        dto.setMerchandiseSubtotal(subtotal);
        dto.setShippingFee(shipFee);
        dto.setDiscountAmount(discount);
        dto.setShippingDiscountAmount(shippingDiscount);

        dto.setTotalAmount(total);
        if (dto.getTotalMoney() == null) dto.setTotalMoney(total); // giữ tương thích

        // Tổng items = tổng quantity
        if (dto.getTotalItems() == null && entity.getOrderDetails() != null) {
            int qtySum = entity.getOrderDetails().stream()
                    .mapToInt(od -> od.getQuantity() == null ? 0 : od.getQuantity())
                    .sum();
            dto.setTotalItems(qtySum);
        }

        // Nhãn mã giảm (nếu có)
        if (discountCpn != null) {
            dto.setDiscountCouponId(discountCpn.getId());
            dto.setDiscountCouponCode(discountCpn.getCode());
        }
        if (shippingCpn != null) {
            dto.setShippingCouponId(shippingCpn.getId());
            dto.setShippingCouponCode(shippingCpn.getCode());
        }
    }

    // ====== DTO -> Entity ======
    @Mapping(source = "orderCode",      target = "idOrders")
    @Mapping(source = "orderDate",      target = "ordersDate")
    @Mapping(source = "customerId",     target = "customer.id")
    @Mapping(source = "paymentId",      target = "paymentMethod.id")
    @Mapping(source = "transportId",    target = "transportMethod.id")
    @Mapping(source = "nameReceiver",   target = "nameReceiver")
    @Mapping(source = "orderDetails",   target = "orderDetails")
    public abstract Order toEntity(OrderDTO dto);

    public abstract List<OrderDTO> toDtoList(List<Order> entities);
    public abstract List<Order> toEntityList(List<OrderDTO> dtos);

    // ===== Helpers: tiền hàng, ship, coupon =====
    protected BigDecimal computeSubtotal(Order order) {
        if (order == null || order.getOrderDetails() == null) return ZERO;
        return order.getOrderDetails().stream()
                .map(od -> {
                    BigDecimal line = od.getTotal();
                    if (line == null) {
                        BigDecimal price = od.getPrice() == null ? ZERO : od.getPrice();
                        int qty = od.getQuantity() == null ? 0 : od.getQuantity();
                        line = price.multiply(BigDecimal.valueOf(qty));
                    }
                    return line;
                })
                .reduce(ZERO, BigDecimal::add);
    }

    protected BigDecimal getShippingFee(Order order) {
        try {
            TransportMethod tm = order.getTransportMethod();
            if (tm != null && tm.getPrice() != null) return tm.getPrice();
        } catch (Exception ignored) {}
        // Nếu Order có field shippingFee, ưu tiên:
        // if (order.getShippingFee()!=null) return order.getShippingFee();
        return ZERO;
    }

    protected BigDecimal computeDiscountForDiscountCoupon(Coupon c, BigDecimal subtotal) {
        if (c == null) return ZERO;
        switch (c.getType()) {
            case PERCENT -> {
                BigDecimal raw = percentage(subtotal, safe(c.getDiscountValue()));
                return cap(raw, c.getMaxDiscountValue());
            }
            case FIXED, CASHBACK_COINS -> {
                return cap(safe(c.getDiscountValue()), c.getMaxDiscountValue());
            }
            default -> { return ZERO; }
        }
    }

    protected BigDecimal computeDiscountForShippingCoupon(Coupon c, BigDecimal shipFee) {
        if (c == null) return ZERO;
        switch (c.getType()) {
            case FREE_SHIPPING -> {
                return cap(safe(shipFee), c.getMaxDiscountValue());
            }
            case SHIPPING_DISCOUNT -> {
                BigDecimal raw = safe(c.getDiscountValue()).min(safe(shipFee));
                return cap(raw, c.getMaxDiscountValue());
            }
            default -> { return ZERO; }
        }
    }

    // Lấy coupon từ Order: nếu bạn chưa tách 2 field, hãy sửa 2 hàm dưới cho khớp
    protected Coupon getDiscountCoupon(Order order) {
        try { return order.getDiscountCoupon(); } catch (Throwable ignored) {}
        try { return order.getCoupon(); } catch (Throwable ignored) {}
        return null;
    }
    protected Coupon getShippingCoupon(Order order) {
        try { return order.getShippingCoupon(); } catch (Throwable ignored) {}
        return null;
    }

    // ===== Small utils =====
    protected BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        if (base == null || pct == null) return ZERO;
        return base.multiply(pct).divide(BigDecimal.valueOf(100));
    }
    protected BigDecimal cap(BigDecimal raw, BigDecimal max) {
        if (raw == null) return ZERO;
        if (max != null && max.signum() > 0) return raw.min(max).max(ZERO);
        return raw.max(ZERO);
    }
    protected BigDecimal safe(BigDecimal v) { return v == null ? ZERO : v; }
}

