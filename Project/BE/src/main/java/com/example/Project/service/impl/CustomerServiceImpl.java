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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

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
}
