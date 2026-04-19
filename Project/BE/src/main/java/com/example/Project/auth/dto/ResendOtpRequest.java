/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 11:59 PM
 * @package: com.example.Project.auth.dto
 */

package com.example.Project.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpRequest {
    @Email
    @NotBlank
    private String email;
}
