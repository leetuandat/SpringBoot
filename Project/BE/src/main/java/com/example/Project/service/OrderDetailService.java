package com.example.Project.service;

import com.example.Project.dto.OrderDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDetailService {
    Page<OrderDetailDTO> findAll(Pageable pageable);
    OrderDetailDTO findById(Long id);
    OrderDetailDTO save(Long orderId, OrderDetailDTO dto);
    OrderDetailDTO update(Long id, OrderDetailDTO dto);
    void deleteById(Long id);
    List<OrderDetailDTO> findByOrderId(Long orderId);
}
