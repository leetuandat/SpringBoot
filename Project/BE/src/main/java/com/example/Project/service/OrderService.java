package com.example.Project.service;

import com.example.Project.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> findAll(Pageable pageable);
    OrderDTO findById(Long id);
    List<OrderDTO> findByCustomerId(Long customerId);
    OrderDTO save(OrderDTO dto, Long userId);
    OrderDTO update(Long id, OrderDTO dto, Long userId);
    void deleteById(Long id, Long userId);
    List<OrderDTO> findByOrderId(String orderId);
    OrderDTO updateStatus(Long id, Boolean isActive, Long userId);
    OrderDTO confirmOrder(Long id, Long userId);
    OrderDTO cancelOrder(Long id, Long userId);
    Object getOrderStatistics(Long userId);
    void activateByOrderCode(String orderCode);
}
