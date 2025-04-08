/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:51 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewsCategoryDTO(
        Long id,
        @NotBlank(message = "News category name is required")
        String name,
        String icon,
        String slug,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive
){}