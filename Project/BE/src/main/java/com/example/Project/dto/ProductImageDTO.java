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

public record ProductImageDTO(
        Long id,
        @NotBlank(message = "Image name cannot be blank")
        String name,
        @NotBlank(message = "Image URL cannot be blank")
        String urlImg,
        @NotNull(message = "productId is required")
        Long productId
){}
