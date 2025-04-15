package com.example.Project.service;

import com.example.Project.dto.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethodDTO> findAll();
    PaymentMethodDTO findById(Long id);
    PaymentMethodDTO save(PaymentMethodDTO dto);
    PaymentMethodDTO update(Long id, PaymentMethodDTO dto);
    void delete(Long id);
    List<PaymentMethodDTO> findByPaymentMethodName(String keyword);
}
