/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/21/2025
 * @time: 03:29 AM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VnPayResult {
    private Long orderId;
    private String orderCode;     // chính là idOrders
    private long paidAmount;      // VND (đã chia 100)
    private String status;        // success | failed
    private String message;
    private String transactionId;
    private boolean success;
}
