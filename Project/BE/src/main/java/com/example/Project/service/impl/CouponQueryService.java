/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 04:46 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.coupon.EligibleCouponDto;
import com.example.Project.entity.Coupon;
import com.example.Project.entity.Order;
import com.example.Project.entity.TransportMethod;
import com.example.Project.repository.CouponRepository;
import com.example.Project.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CouponQueryService {
    private final CouponRepository couponRepo;
    private final OrderDetailRepository orderDetailRepo;

    public List<EligibleCouponDto> listEligibleForCurrentCart(Order cart) {
        var now = LocalDateTime.now(ZoneOffset.UTC);
        var all = couponRepo.findAllActiveNow(now);

        // Tổng tiền hàng & phí ship hiện tại
        var details = orderDetailRepo.findByOrderId(cart.getId());
        BigDecimal subtotal = details.stream()
                .map(d -> d.getTotal()!=null? d.getTotal()
                        : d.getPrice().multiply(BigDecimal.valueOf(d.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal ship = getShippingFee(cart);

        // Lọc & ước lượng mức giảm
        return all.stream()
                .filter(c -> isEligibleBasic(c, cart, subtotal))
                .map(c -> new EligibleCouponDto(
                        c.getId(), c.getCode(), c.getType().name(), c.getScope().name(),
                        c.getDescription(), c.getDiscountValue(), c.getMaxDiscountValue(),
                        c.getMinOrderAmount(),
                        c.isShippingCoupon()? "SHIPPING" : "DISCOUNT",
                        c.isShippingCoupon()
                                ? computeDiscountForShippingCoupon(c, ship)
                                : computeDiscountForDiscountCoupon(c, subtotal)
                ))
                // sắp xếp coupon giảm nhiều lên trước
                .sorted(Comparator.comparing(EligibleCouponDto::getEstimatedSavings).reversed())
                .toList();
    }

    private boolean isEligibleBasic(Coupon c, Order cart, BigDecimal subtotal) {
        if (c.getMinOrderAmount()!=null && subtotal.compareTo(c.getMinOrderAmount())<0) return false;
        return true;
    }

    private BigDecimal getShippingFee(Order cart) {
        try {
            TransportMethod tm = cart.getTransportMethod();
            if (tm != null && tm.getPrice() != null) return tm.getPrice();
        } catch (Exception ignored) {}
        // Nếu chưa chọn phương thức vận chuyển => coi như 0
        return BigDecimal.ZERO;
    }

    private BigDecimal computeDiscountForDiscountCoupon(Coupon c, BigDecimal merchandiseSubtotal) {
        if (c == null) return BigDecimal.ZERO;
        BigDecimal max = safe(c.getMaxDiscountValue());
        BigDecimal raw = switch (c.getType()) {
            case PERCENT ->
                    percentage(merchandiseSubtotal, safe(c.getDiscountValue())); // % of subtotal
            case FIXED, CASHBACK_COINS ->
                    safe(c.getDiscountValue());
            default -> BigDecimal.ZERO;
        };
        BigDecimal capped = (max.signum() > 0) ? raw.min(max) : raw;
        return capped.max(BigDecimal.ZERO);
    }

    private BigDecimal computeDiscountForShippingCoupon(Coupon c, BigDecimal shippingFee) {
        if (c == null) return BigDecimal.ZERO;
        BigDecimal max = safe(c.getMaxDiscountValue());
        BigDecimal raw = switch (c.getType()) {
            case FREE_SHIPPING -> safe(shippingFee); // tối đa = phí ship
            case SHIPPING_DISCOUNT -> safe(c.getDiscountValue()).min(safe(shippingFee));
            default -> BigDecimal.ZERO;
        };
        BigDecimal capped = (max.signum() > 0) ? raw.min(max) : raw;
        return capped.max(BigDecimal.ZERO);
    }

    private BigDecimal percentage(BigDecimal base, BigDecimal percent) {
        if (base == null || percent == null) return BigDecimal.ZERO;
        return base.multiply(percent).divide(BigDecimal.valueOf(100));
    }
    private BigDecimal safe(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }
}


