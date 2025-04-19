package com.example.Project.service;

import com.example.Project.dto.PaymentMethodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentMethodService {
    Page<PaymentMethodDTO> findAll(Pageable pageable);
    PaymentMethodDTO findById(Long id);
    PaymentMethodDTO save(PaymentMethodDTO dto);
    PaymentMethodDTO update(Long id, PaymentMethodDTO dto);
    void delete(Long id);
    Page<PaymentMethodDTO> findByPaymentMethodName(String keyword, Pageable pageable);
}
