/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:51 PM
 * @package: com.example.LabGuild04.dto
 */

package com.example.LabGuild04.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class employeeDTO {
    Long id;

    @Size(min = 3, max = 25,message = "Họ tên phải có độ dài từ 3 - 25 ký tự")
    String fullName;
    @Min(value = 18, message = "Tuổi phải từ 18 trờ lên")
    @Max(value = 60, message = "Tuổi không được quá 60")
    int age;

    @Positive(message = "Lương phải lớn hơn 0")
    double salary;
}
