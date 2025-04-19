package com.example.Project.service;

import com.example.Project.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<OrderDTO> findAll(Pageable pageable);
    OrderDTO findById(Long id);
    OrderDTO save(OrderDTO dto);
    OrderDTO update(Long id, OrderDTO dto);
    void deleteById(Long id);
    List<OrderDTO> findByOrderId(String orderId);
}
