/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 02:31 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto.coupon;

import com.example.Project.entity.Coupon.CouponType;
import com.example.Project.entity.Coupon.CodeType;
import com.example.Project.entity.Coupon.CouponScope;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateCouponRequest {
    @NotNull private CodeType codeType;
    private String code;

    @NotNull private CouponType type;
    private CouponScope scope;
    private Long shopId;

    @NotNull private LocalDateTime startAtUtc;
    @NotNull private LocalDateTime endAtUtc;

    private String description;

    // Values
    private BigDecimal discountValue;
    private BigDecimal maxDiscountValue;
    private BigDecimal minOrderAmount;

    // Limits & budget
    private Integer usageLimitTotal;
    private Integer usageLimitPerUser;
    private BigDecimal budgetCap;

    // Customer constraints
    private Boolean newUserOnly = false;
    private Boolean firstOrderOnly = false;

    // Scope relations (optional)
    private Set<Long> productIds;
    private Set<Long> categoryIds;
    private Set<Long> paymentMethodIds;

    // Trạng thái
    private Boolean isActive = true;
}
