/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson04
 * @date: 2/28/2025
 * @time: 07:16 PM
 * @package: com.example.Lesson04.entity
 */

package com.example.Lesson04.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@Builder

public class UserDTO {
//    @NotNull(message = "Name không được để null")
//    @NotEmpty(message = "Name không được rỗng")
    @Size(min = 2, max = 50, message = "Name có độ dài trong khoảng 2 đến 50 ký tự")
    private String name;

    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Age không null")
    private int age;
}
