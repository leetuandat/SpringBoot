/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 11:57 PM
 * @package: com.example.Project.auth.dto
 */

package com.example.Project.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterInitRequest {
    @Email
    @NotBlank
    private String email;
    @Size(min=6, max=100) private String password;
    @Size(min=2, max=100) private String fullName;
}

