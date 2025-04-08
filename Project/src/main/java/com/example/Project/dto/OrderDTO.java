/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:52 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        @NotBlank(message = "orderCode cannot be blank")
        String orderCode,
        LocalDateTime orderDate,
        @NotNull(message = "customerId is required")
        Long customerId,
        @DecimalMin(value = "0.0", message = "Total money cannot be negative")
        BigDecimal totalMoney,
        String notes,
        String nameReceiver,
        String address,
        @Email(message = "Email is not valid")
        String email,
        String phone,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive
){}
