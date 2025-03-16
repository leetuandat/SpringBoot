/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson07
 * @date: 3/14/2025
 * @time: 06:53 PM
 * @package: com.example.Lesson07.entity
 */

package com.example.Lesson07.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 3, message = "Họ tên tối thiểu có 3 ký tự")
    String fullName;

    @NotBlank(message = "Tài khoản không để trống")
    @Size(min = 3, message = "Tài khoản có tối thiểu 3 ký tự")
    @Column(unique = true, nullable = false)
    String userName;

    @NotBlank(message = "Mật khẩu không để trống")
    @Size(min = 6, message = "Mật khẩu có ít nhất 6 ký tự")
    String password;

    @NotBlank(message = "Email không để trống")
    @Email(message = "Email không đúng định dạng")
    String email;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Điện thoại chỉ chứa ký tự số, độ dài trong khoảng 10-15")
    String phone;

    String address;
    Boolean isActive;

}
