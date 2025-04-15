/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:50 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderDetailDTO(
        Long id,
        @NotNull(message = "ProductId is required")
        Long productId,
        @DecimalMin(value = "0.0", message = "Price cannot be negative")
        BigDecimal price,
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity,
        @DecimalMin(value = "0.0", message = "Total must be >= 0")
        BigDecimal total
){}