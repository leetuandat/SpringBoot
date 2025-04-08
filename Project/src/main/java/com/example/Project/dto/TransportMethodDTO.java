/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:49 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransportMethodDTO(
        Long id,
        @NotBlank(message = "Transport method name cannot be blank")
        String name,
        String notes,
        @NotNull(message = "isActive cannot be null")
        Boolean isActive
){}
