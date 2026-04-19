/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:36 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import com.example.Project.config.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ORDERS", uniqueConstraints = @UniqueConstraint(columnNames = "idOrders"))
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** vnp_TxnRef tối đa 32 ký tự; bạn đang để 50, service VNPAY có thể cắt. */
    @Column(name = "idOrders", length = 50, unique = true, nullable = false)
    private String idOrders;

    private LocalDateTime ordersDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCustomer", foreignKey = @ForeignKey(name = "fk_orders_customer"))
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPayment", foreignKey = @ForeignKey(name = "fk_orders_payment_method"))
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTransport", foreignKey = @ForeignKey(name = "fk_orders_transport_method"))
    private TransportMethod transportMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "paid_amount")
    private Long paidAmount;              // VND

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCoupon", foreignKey = @ForeignKey(name = "fk_orders_coupon"))
    private Coupon coupon; // nếu dùng 2 coupon riêng ở dưới có thể bỏ

    // ====== THÀNH PHẦN TIỀN (mới) ======
    @Column(name = "sub_total", precision = 19, scale = 2)
    private BigDecimal subTotal;                       // tạm tính tiền hàng

    @Column(name = "shipping_fee", precision = 19, scale = 2)
    private BigDecimal shippingFee;                    // phí ship gốc

    @Column(name = "discount_amount", precision = 19, scale = 2)
    private BigDecimal discountAmount;                 // giảm giá hàng

    @Column(name = "shipping_discount_amount", precision = 19, scale = 2)
    private BigDecimal shippingDiscountAmount;         // giảm phí ship

    @Column(name = "total_money", precision = 19, scale = 2)
    private BigDecimal totalMoney;                     // tổng cuối

    @Lob
    private String notes;

    @Column(length = 250)
    private String nameReceiver;

    @Column(length = 500)
    private String address;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false)
    private Boolean isDelete;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_coupon_id", foreignKey = @ForeignKey(name = "fk_orders_discount_coupon"))
    private Coupon discountCoupon;   // %/FIXED/CASHBACK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_coupon_id", foreignKey = @ForeignKey(name = "fk_orders_shipping_coupon"))
    private Coupon shippingCoupon;   // FREE_SHIPPING/SHIPPING_DISCOUNT

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    public long getTotalMoneyValue() {
        return totalMoney != null ? totalMoney.longValue() : 0L;
    }



    @PrePersist
    protected void onCreate() {
        if (this.ordersDate == null) this.ordersDate = LocalDateTime.now();
        if (this.isDelete == null) this.isDelete = false;
        if (this.isActive == null) this.isActive = true;

        if (this.subTotal == null) this.subTotal = BigDecimal.ZERO;
        if (this.shippingFee == null) this.shippingFee = BigDecimal.ZERO;
        if (this.discountAmount == null) this.discountAmount = BigDecimal.ZERO;
        if (this.shippingDiscountAmount == null) this.shippingDiscountAmount = BigDecimal.ZERO;
        if (this.totalMoney == null) this.totalMoney = BigDecimal.ZERO;
    }
}

