/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:52 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerDTO(
        Long id,
        @NotBlank(message = "Customer name is required")
        String name,
        @NotBlank(message = "Username cannot be blank")
        String username,
        String password,
        String address,
        @Email(message = "Email is not valid")
        String email,
        String phone,
        String avatar,
//        @NotNull(message = "isActive cannot be null")
        Boolean isActive,
        String role
){}