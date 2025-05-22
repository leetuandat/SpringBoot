/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:34 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER")

public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250)
    private String name;

    @Column(length = 50)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(length = 500)
    private String address;

    @Column(length = 150)
    private String email;

    @Column(length = 50)
    private String phone;

    private String role;

    @Column(columnDefinition = "LONGTEXT")
    private String avatar;

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

