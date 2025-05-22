/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/16/2025
 * @time: 09:11 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.dto.CustomerUpdateDTO;
import com.example.Project.entity.Customer;
import com.example.Project.mapper.CustomerMapper;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> findById(@PathVariable Long id) {
        CustomerDTO customerDTO = customerService.findById(id);
        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> save(@RequestBody CustomerDTO customerDTO) {
        CustomerDTO createdCustomer = customerService.save(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> update(@PathVariable Long id, @RequestBody CustomerUpdateDTO dto) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setAvatar(dto.avatar());
        Customer updated = customerRepository.save(customer);
        return ResponseEntity.ok(customerMapper.toDto(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomer(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(customerService.findByCustomerName(keyword, pageable));
    }
}
