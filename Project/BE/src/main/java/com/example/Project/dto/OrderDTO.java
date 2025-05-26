package com.example.Project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO cho đơn hàng, hỗ trợ builder pattern bằng Lombok
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

        private Long id;

        @NotBlank(message = "orderCode cannot be blank")
        private String orderCode;

        private LocalDateTime orderDate;

        @NotNull(message = "customerId is required")
        private Long customerId;

        @NotNull(message = "paymentId is required")
        private Long paymentId;

        @NotNull(message = "transportId is required")
        private Long transportId;

        @DecimalMin(value = "0.0", message = "Total money cannot be negative")
        private BigDecimal totalMoney;

        private String notes;

        private String nameReceiver;

        private String address;

        @Email(message = "Email is not valid")
        private String email;

        private String phone;

        @NotNull(message = "isActive cannot be null")
        private Boolean isActive;

        private List<OrderDetailDTO> orderDetails;
        private Integer totalItems;
}
