/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/24/2025
 * @time: 08:15 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "COUPONS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private BigDecimal discountAmount; // hoặc discountPercent nếu là phần trăm

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(length = 500)
    private String description;

    @PrePersist
    protected void onCreate() {
        if (isActive == null) isActive = true;
    }
}

