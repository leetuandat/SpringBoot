/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:52 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.NotNull;

public record ProductConfigDTO(
        Long id,
        @NotNull(message = "productId is required")
        Long productId,
        @NotNull(message = "configId is required")
        Long configId,
        String value
){}