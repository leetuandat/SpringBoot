package com.example.Project.repository;

import com.example.Project.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdOrdersContainingIgnoreCase(String idOrders);
}
