/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:27 PM
 * @package: com.example.LabGuild04.dto
 */

package com.example.LabGuild04.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Valid
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KhoaDTO {
    @NotBlank(message = "Mã khoa không được để trống")
    @Size(min = 2, message = "Mã khoa phải có ít nhất 2 ký tự")
    String Makh;

    @Size(min = 5, max = 25, message = "Tên khoa phải từ 5 đến 25 ký tự")
    String tenkh;
}
