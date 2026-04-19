/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 04:45 PM
 * @package: com.example.Project.dto.coupon
 */

package com.example.Project.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class EligibleCouponDto {
    private Long id;
    private String code;                // có thể null với UNIQUE_POOL/AUTO_APPLY
    private String type;                // PERCENT/FIXED/...
    private String scope;               // PLATFORM/SHOP
    private String description;
    private BigDecimal discountValue;
    private BigDecimal maxDiscountValue;
    private BigDecimal minOrderAmount;
    private String group;               // "DISCOUNT" hoặc "SHIPPING"
    private BigDecimal estimatedSavings;// ước lượng tiền giảm trên giỏ hiện tại
}
