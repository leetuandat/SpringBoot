/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lab04
 * @date: 2/28/2025
 * @time: 07:58 PM
 * @package: com.example.Lab04.entity
 */

package com.example.Lab04.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
//import lombok.Generated;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Getter
@Setter

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String username;
    String password;
    String fullName;
    LocalDate birthDay;
    String email;
    String phone;
    int age;
    Boolean status;
}
