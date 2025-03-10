/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:35 PM
 * @package: com.example.LabGuild04.dto
 */

package com.example.LabGuild04.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class MonHocDTO {
    @Size(min = 2, message = "Mã môn học phải có ít nhất 2 ký tự")
    String Mamh;

    @Size(min = 5, max = 50, message = "Tên môn học phải có từ 5 đến 50 ký tự")
    String Tenmh;

    @Min(value = 45, message = "Số tiết phải lớn hơn hoặc bằng 45")
    @Max(value = 75, message = "Số tiết phải nhỏ hơn bằng 75")
    int sotiet;
}
