/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:51 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDTO(
        Long id,
        @NotBlank(message = "Product name is required")
        String name,
        Long authorId,
        String authorName,
        String categoryName,
        String description,
        @DecimalMin(value = "0.0", message = "Price cannot be negative")
        BigDecimal price,
        @Min(value = 0, message = "Quantity cannot be negative")
        Integer quantity,
        String image,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive,
        String slug,
        String contents,
        LocalDateTime createdDate,
        LocalDateTime updatedDate,

        String notes,
        String metaTitle,
        String metaKeyword,
        String metaDescription,
        Long categoryId

        ){}