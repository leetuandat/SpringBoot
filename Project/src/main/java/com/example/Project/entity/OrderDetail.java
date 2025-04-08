/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/8/2025
 * @time: 10:41 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ORDERS_DETAILS")
@Data
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idOrd", insertable = false, updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "idProduct", insertable = false, updatable = false)
    private Product product;


    private BigDecimal price;

    private Integer qty;

    private BigDecimal total;

    @Column(name = "return_qty")
    private Integer returnQty;
}
