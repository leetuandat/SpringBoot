package com.example.Project.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ShippingOptionDTO {
    private Long id;                 // id TransportMethod
    private String name;             // tên phương thức
    private String code;             // "STANDARD" | "EXPRESS" (suy từ name)
    private Boolean supported;       // EXPRESS false nếu > 15km
    private BigDecimal fee;          // null nếu chưa có distance cho EXPRESS
    private String note;             // ví dụ: "Không hỗ trợ hỏa tốc ngoài 15 km"
}
