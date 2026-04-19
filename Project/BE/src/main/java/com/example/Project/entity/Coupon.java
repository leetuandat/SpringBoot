package com.example.Project.entity;

import com.example.Project.entity.Category;
import com.example.Project.entity.PaymentMethod;
import com.example.Project.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "COUPON",
        uniqueConstraints = @UniqueConstraint(name = "uk_coupon_code", columnNames = {"code"}),
        indexes = {
                @Index(name = "idx_coupon_active", columnList = "is_active"),
                @Index(name = "idx_coupon_time", columnList = "start_at_utc,end_at_utc")
        }
)
public class Coupon {

    // ===== Enums =====
    public enum CouponType { PERCENT, FIXED, SHIPPING_DISCOUNT, FREE_SHIPPING, CASHBACK_COINS }
    public enum CouponScope { PLATFORM, SHOP }
    public enum CodeType { SINGLE, UNIQUE_POOL, AUTO_APPLY }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Có thể null khi CodeType = UNIQUE_POOL/AUTO_APPLY */
    @Size(max = 80)
    @Column(length = 80)
    private String code;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodeType codeType;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CouponType type;

    @NotNull @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponScope scope;

    /** null nếu scope=PLATFORM */
    private Long shopId;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    // Window UTC
    @NotNull @Column(name = "start_at_utc", nullable = false)
    private LocalDateTime startAtUtc;

    @NotNull @Column(name = "end_at_utc", nullable = false)
    private LocalDateTime endAtUtc;

    // Values & conditions
    @Digits(integer = 17, fraction = 2)
    @Column(precision = 19, scale = 2)
    private BigDecimal discountValue;

    @Digits(integer = 17, fraction = 2)
    @Column(precision = 19, scale = 2)
    private BigDecimal maxDiscountValue;

    @Digits(integer = 17, fraction = 2)
    @Column(precision = 19, scale = 2)
    private BigDecimal minOrderAmount;

    // Limits & budget
    @Min(0) private Integer usageLimitTotal;
    @Min(0) private Integer usageLimitPerUser;

    @Builder.Default
    @Min(0) @Column(nullable = false)
    private Integer usedCountTotal = 0;

    @Digits(integer = 17, fraction = 2)
    @Column(precision = 19, scale = 2)
    private BigDecimal budgetCap;

    @Builder.Default
    @Digits(integer = 17, fraction = 2)
    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal budgetUsed = BigDecimal.ZERO;

    // Customer constraints
    @Builder.Default @Column(nullable = false)
    private Boolean newUserOnly = false;
    @Builder.Default @Column(nullable = false)
    private Boolean firstOrderOnly = false;

    // Scope relations
    @ManyToMany
    @JoinTable(
            name = "COUPON_PRODUCTS",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;

    @ManyToMany
    @JoinTable(
            name = "COUPON_CATEGORIES",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @ManyToMany
    @JoinTable(
            name = "COUPON_PAYMENT_METHODS",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private Set<PaymentMethod> paymentMethods;

    // Audit
    @CreationTimestamp @Column(updatable = false)
    private LocalDateTime createdAtUtc;
    @UpdateTimestamp
    private LocalDateTime updatedAtUtc;

    // Helpers
    @Transient
    public boolean isShippingCoupon() {
        return type == CouponType.FREE_SHIPPING || type == CouponType.SHIPPING_DISCOUNT;
    }
    @Transient
    public boolean isDiscountCoupon() {
        return type == CouponType.PERCENT || type == CouponType.FIXED || type == CouponType.CASHBACK_COINS;
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        // 1) Thời gian
        if (startAtUtc == null || endAtUtc == null || !endAtUtc.isAfter(startAtUtc)) {
            throw new IllegalArgumentException("endAtUtc phải sau startAtUtc.");
        }

        // 2) Single-store: luôn PLATFORM, không dùng shopId
        scope = CouponScope.PLATFORM;
        shopId = null;

        // 3) Chuẩn hoá cờ trạng thái
        if (isActive == null)  isActive  = true;
        if (isDelete == null)  isDelete  = false;

        // 4) Bắt buộc type/codeType
        if (type == null)     throw new IllegalArgumentException("type bắt buộc.");
        if (codeType == null) throw new IllegalArgumentException("codeType bắt buộc.");

        // 5) Chuẩn hoá và kiểm tra code
        if (code != null) {
            code = code.trim();
            if (code.isEmpty()) code = null;
            else code = code.toUpperCase(); // tuỳ chính sách, có thể bỏ
        }
        if (codeType == CodeType.SINGLE) {
            if (code == null) {
                throw new IllegalArgumentException("CodeType=SINGLE cần 'code'.");
            }
        } else {
            // UNIQUE_POOL / AUTO_APPLY: không lưu code ở COUPON
            code = null;
        }

        // 6) Kiểm tra giá trị theo loại
        switch (type) {
            case PERCENT -> {
                if (discountValue == null)
                    throw new IllegalArgumentException("PERCENT cần discountValue (%).");
                var v = discountValue;
                if (v.compareTo(BigDecimal.ZERO) <= 0 || v.compareTo(BigDecimal.valueOf(100)) > 0)
                    throw new IllegalArgumentException("discountValue (%) phải trong (0,100].");
            }
            case FIXED, SHIPPING_DISCOUNT -> {
                if (discountValue == null || discountValue.compareTo(BigDecimal.ZERO) <= 0)
                    throw new IllegalArgumentException(type + " cần discountValue > 0.");
            }
            case FREE_SHIPPING, CASHBACK_COINS -> {
                // FREE_SHIPPING: không cần discountValue; có cũng bỏ qua cho sạch
                if (type == CouponType.FREE_SHIPPING) discountValue = null;
            }
        }

        // 7) Điều kiện đơn & ngân sách/giới hạn (nếu có các field này)
        if (minOrderAmount != null && minOrderAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("minOrderAmount không được âm.");

        if (usageLimitTotal != null && usageLimitTotal < 0)
            throw new IllegalArgumentException("usageLimitTotal không hợp lệ.");
        if (usageLimitPerUser != null && usageLimitPerUser < 0)
            throw new IllegalArgumentException("usageLimitPerUser không hợp lệ.");

        if (budgetCap != null && budgetCap.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("budgetCap không hợp lệ.");
        if (budgetUsed == null || budgetUsed.compareTo(BigDecimal.ZERO) < 0)
            budgetUsed = BigDecimal.ZERO;
    }

}
