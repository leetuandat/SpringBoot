/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:20 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import com.example.Project.entity.Category;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUCT")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String name;

    @Lob
    private String description;

    @Lob
    private String notes;

    @Column(length = 550)
    private String image;

    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;

    @Lob
    private String contents;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quatity;

    @Column(length = 160)
    private String slug;

    @Column(length = 100)
    private String metaTitle;

    @Column(length = 500)
    private String metaKeyword;

    @Column(length = 500)
    private String metaDescription;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private Long createdBy;

    private Long updatedBy;

    @Column(nullable = false)
    private Boolean isDelete;

    @Column(nullable = false)
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;

        if (this.price == null) {
            this.price = BigDecimal.ZERO;
        }

        if (this.quatity == null) {
            this.quatity = 0;
        }

        if (this.isDelete == null) {
            this.isDelete = false;
        }

        if (this.isActive == null) {
            this.isActive = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
