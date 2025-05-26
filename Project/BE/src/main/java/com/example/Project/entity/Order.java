/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:36 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

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

    @Column(name = "idOrders", length = 50, unique = true, nullable = false)
    private String idOrders;

    private LocalDateTime ordersDate;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "idPayment")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "idTransport")
    private TransportMethod transportMethod;

    @ManyToOne
    @JoinColumn(name = "idCoupon")
    private Coupon coupon;


    private BigDecimal totalMoney;

    @Lob
    private String notes;

    @Column(length = 250)
    private String nameReceiver; // ← Đã sửa đúng tên

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @PrePersist
    protected void onCreate() {
        if (this.ordersDate == null) {
            this.ordersDate = LocalDateTime.now();
        }
        if (this.isDelete == null) {
            this.isDelete = false;
        }
        if (this.isActive == null) {
            this.isActive = true;
        }
    }
}

