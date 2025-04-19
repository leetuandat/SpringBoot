/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/10/2025
 * @time: 10:30 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.PaymentMethodDTO;
import com.example.Project.entity.PaymentMethod;
import com.example.Project.mapper.PaymentMethodMapper;
import com.example.Project.repository.PaymentMethodRepository;
import com.example.Project.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    @Override
    public Page<PaymentMethodDTO> findAll(Pageable pageable) {
        return (paymentMethodRepository.findAll(pageable)).map(paymentMethodMapper::toDto);
    }

    @Override
    public PaymentMethodDTO findById(Long id) {
        PaymentMethod entity = paymentMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment method not found"));
        return paymentMethodMapper.toDto(entity);
    }

    @Override
    public PaymentMethodDTO save(PaymentMethodDTO dto) {
        PaymentMethod entity = paymentMethodMapper.toEntity(dto);
        entity.setIsDelete(false);
        PaymentMethod saved  = paymentMethodRepository.save(entity);
        return paymentMethodMapper.toDto(saved);
    }

    @Override
    public PaymentMethodDTO update(Long id, PaymentMethodDTO dto) {
        PaymentMethod entity = paymentMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment method not found"));
        entity.setName(dto.name());
        entity.setNotes(dto.notes());
        entity.setIsActive(dto.isActive());
        PaymentMethod updated = paymentMethodRepository.save(entity);
        return paymentMethodMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        PaymentMethod entity = paymentMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment method not found"));
        entity.setIsDelete(true);
        paymentMethodRepository.save(entity);
    }

    @Override
    public Page<PaymentMethodDTO> findByPaymentMethodName(String keyword, Pageable pageable) {
        Page<PaymentMethod> methods = paymentMethodRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return methods.map(paymentMethodMapper::toDto);
    }
}
