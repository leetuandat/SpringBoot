/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06.2
 * @date: 3/18/2025
 * @time: 04:44 PM
 * @package: com.example.LabGuild062.dto
 */

package com.example.LabGuild062.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerDTO {
    Long id;
    String username;
    String fullName;
    String address;
    String phone;
    String email;
    LocalDate birthDay;
    boolean active;

}
