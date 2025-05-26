package com.example.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
        Long id,
        @NotBlank(message = "Category name is required")
        String name,
        String icon,
        String slug,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive,
        String metaDescription
){}
