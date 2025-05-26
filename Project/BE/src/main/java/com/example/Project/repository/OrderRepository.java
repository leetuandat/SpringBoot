package com.example.Project.repository;

import com.example.Project.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByIdOrdersContainingIgnoreCase(String idOrders);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByIdOrdersContaining(String idOrders);
    long countByCustomerId(Long customerId);
    long countByCustomerIdAndIsActiveTrue(Long customerId);
    long countByCustomerIdAndIsActiveFalse(Long customerId);
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    List<Order> findByCustomerIdAndIsActiveFalseAndIsDeleteFalse(Long customerId);
    Optional<Order> findByIdOrders(String idOrders);
}
