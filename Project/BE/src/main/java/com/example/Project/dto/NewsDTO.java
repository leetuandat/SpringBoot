/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:52 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewsDTO(
        Long id,
        @NotBlank(message = "News name cannot be blank")
        String name,
        String description,
        String image,
        @NotNull(message = "categoryId is required")
        Long categoryId,
        String slug,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive
){}