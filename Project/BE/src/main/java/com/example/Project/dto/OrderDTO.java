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

        // Mã đơn: nếu bạn tạo luôn khi tạo cart thì giữ @NotBlank OK,
        // còn nếu khởi tạo cart chưa có code, hãy dùng Validation Group (Create/Update)
        private String orderCode;

        private LocalDateTime orderDate;

        @NotNull(message = "customerId is required")
        private Long customerId;

        // Payment / Transport
        @NotNull(message = "paymentId is required")
        private Long paymentId;
        private String paymentMethodName;    // read-only (tuỳ)

        @NotNull(message = "transportId is required")
        private Long transportId;
        private String transportMethodName;  // read-only (tuỳ)

        private String notes;

        // ==== TIỀN TỆ ====
        /** Tạm tính tiền hàng (chưa ship & chưa giảm) */
        private BigDecimal merchandiseSubtotal;

        /** Phí vận chuyển gốc (chưa giảm ship) */
        private BigDecimal shippingFee;

        /** Tổng tiền giảm từ coupon loại giảm giá (PERCENT/FIXED/...) */
        private BigDecimal discountAmount;

        /** Tổng tiền giảm trên phí vận chuyển (FREE_SHIPPING/SHIPPING_DISCOUNT) */
        private BigDecimal shippingDiscountAmount;

        /** Tổng cuối cùng; giữ totalMoney để tương thích cũ */
        private BigDecimal totalMoney;  // giữ
        private BigDecimal totalAmount; // alias mới (tuỳ chọn)

        // ==== COUPONS (để FE hiển thị nhãn) ====
        private Long discountCouponId;
        private String discountCouponCode;
        private Long shippingCouponId;
        private String shippingCouponCode;

        // ==== NGƯỜI NHẬN ====
        private String nameReceiver;
        private String address;
        @Email(message = "Email is not valid")
        private String email;
        private String phone;

        @NotNull(message = "isActive cannot be null")
        private Boolean isActive;

        private List<OrderDetailDTO> orderDetails;
        private Integer totalItems;

        @Data
        public static class CouponLiteDTO {
                private Long id;
                private String code;
                private String type;              // PERCENT/FIXED/FREE_SHIPPING/SHIPPING_DISCOUNT
                private BigDecimal discountValue;
                private BigDecimal maxDiscountValue;
                private BigDecimal minOrderAmount;
        }
}
