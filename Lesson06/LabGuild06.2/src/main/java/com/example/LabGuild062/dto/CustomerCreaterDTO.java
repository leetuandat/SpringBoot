/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 04:51 PM
 * @package: com.example.LabGuild062.dto
 */

package com.example.LabGuild062.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreaterDTO {
    @NotBlank(message = "Username is required")
    String username;

    @NotBlank(message = "Password is required")
    String password;

    @NotBlank(message = "Full name is required")
    String fullName;

    String address;
    String phone;
    String email;
    String birthDay;
    boolean active = true;
}
