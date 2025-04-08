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

@Entity
@Table(name = "PRODUCT_CONFIG")
@Data
public class ProductConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idProduct", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idConfig", insertable = false, updatable = false)
    private Configuration configuration;


    @Lob
    private String value;
}

