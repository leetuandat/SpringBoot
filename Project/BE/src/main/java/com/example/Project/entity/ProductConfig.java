/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:30 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PRODUCT_CONFIG")

public class ProductConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "idProduct", nullable = false)
    private Long idProduct;

    @Column(name = "idConfig", nullable = false)
    private Long idConfig;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idProduct", insertable = false, updatable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idConfig", insertable = false, updatable = false)
    private Configuration configuration;


    @Lob
    private String value;
}

