/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 05:09 PM
 * @package: com.example.LabGuild062.dto
 */

package com.example.LabGuild062.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PasswordChangeDTO {
    @NotBlank(message = "Old password is required")
    String oldPassword;

    @NotBlank(message = "New password is required")
    String newPassword;
}
