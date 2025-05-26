package com.example.Project.repository;

import com.example.Project.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
    OrderDetail findByOrderIdAndProductId(Long orderId, Long productId);
    int countByOrderId(Long orderId);
}
