/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:35 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSPORT_METHOD")
@Data
public class TransportMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250)
    private String name;

    @Lob
    private String notes;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

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
