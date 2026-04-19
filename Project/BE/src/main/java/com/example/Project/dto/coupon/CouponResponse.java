/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 02:34 PM
 * @package: com.example.Project.dto.coupon
 */

package com.example.Project.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CouponResponse {
    private Long id;
    private String code;
}
