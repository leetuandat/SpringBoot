/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lab04
 * @date: 2/28/2025
 * @time: 08:04 PM
 * @package: com.example.Lab04.dto
 */

package com.example.Lab04.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.Generated;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsersDTO {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(unique = true)
    String username;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{8,30}",
                message = "Password must be contains at least one letter and one number")
    String password;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    String fullName;

    @Past(message = "Birthday must be in the past")
    LocalDate birthDay;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    @Column(unique = true)
    String email;

    @Pattern(regexp = "^\\+?[0-9.  ()-]{7,25}$", message = "Phone number is invalid")
    @NotBlank(message = "Phone number cannot be blank")
    String phone;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than or equal to 100")
    int age;

    @NotNull(message = "Status cannot be null")
    Boolean status;
}
