/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 05:24 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import com.example.Project.mapper.CustomerMapper;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;


    @Override
    @Transactional
    public CustomerDTO registerWithOtp(CustomerDTO dto) {
        // 1) kiểm tra email
        var existingOpt = customerRepository.findByEmailIgnoreCase(dto.email());
        if (existingOpt.isPresent()) {
            var existing = existingOpt.get();
            if (Boolean.TRUE.equals(existing.isEmailVerified())) {
                throw new IllegalStateException("Email đã tồn tại.");
            } else {
                // chưa xác thực email
                throw new IllegalStateException("Email đã đăng ký nhưng chưa xác thực. Vui lòng kiểm tra email để nhập OTP.");
            }
        }

        // 2) tạo user
        Customer c = new Customer();
        c.setName(dto.name());
        c.setUsername(dto.username());
        if (dto.password() != null) c.setPassword(passwordEncoder.encode(dto.password()));
        c.setAddress(dto.address());
        c.setEmail(dto.email());
        c.setPhone(dto.phone());
        c.setAvatar(dto.avatar());
        c.setRole(dto.role());

        // 🔑 Cách B:
        c.setEmailVerified(false); // chặn login vì chưa xác thực email
        c.setIsActive(true);       // vẫn active, admin có thể khoá sau

        Customer saved = customerRepository.save(c);

        // 3) phát hành + gửi OTP
        otpService.issueRegisterOtpForExistingCustomer(saved.getId(), saved.getEmail());

        // 4) trả DTO
        return customerMapper.toDto(saved);
    }

    @Override
    public Page<CustomerDTO> findAll(Pageable pageable) {
        return (customerRepository.findAll(pageable)).map(customerMapper::toDto);
    }

    @Override
    public CustomerDTO findById(Long id) {
        Customer entity = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        return customerMapper.toDto(entity);
    }

    @Override
    public CustomerDTO save(CustomerDTO dto) {
        Customer entity = customerMapper.toEntity(dto);
        entity.setIsDelete(false);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO dto) {
        Customer entity = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        entity.setName(dto.name());
        entity.setUsername(dto.username());
        entity.setAddress(dto.address());
        entity.setEmail(dto.email());
        entity.setPhone(dto.phone());
        entity.setAvatar(dto.avatar());
        entity.setIsActive(dto.isActive());
        Customer uploaded = customerRepository.save(entity);
        return customerMapper.toDto(uploaded);
    }

    @Override
    public void delete(Long id) {
        Customer entity = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer Not Found"));
        entity.setIsDelete(true);
        customerRepository.save(entity);
    }

    @Override
    public Page<CustomerDTO> findByCustomerName(String keyword, Pageable pageable) {
        Page<Customer> customers = customerRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return customers.map(customerMapper::toDto);
    }

    @Override
    public Customer createPending(String email, String rawPassword, String fullName) {
        var c = new Customer();
        c.setEmail(email);
        if (rawPassword != null) {
            c.setPassword(passwordEncoder.encode(rawPassword));
        }
        c.setName(fullName);
        c.setEmailVerified(false);
        return customerRepository.save(c);
    }

    @Override
    public void markEmailVerified(String email) {
        var c = customerRepository.findByEmailIgnoreCase(email).orElseThrow();
        c.setEmailVerified(true);
        customerRepository.save(c);
    }
}
