/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 04:32 PM
 * @package: com.example.Project.service
 */

package com.example.Project.service;

import com.example.Project.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Page<CustomerDTO> findAll(Pageable pageable);
    CustomerDTO findById(Long id);
    CustomerDTO save(CustomerDTO dto);
    CustomerDTO update(Long id, CustomerDTO dto);
    void delete(Long id);
    Page<CustomerDTO> findByCustomerName(String keyword, Pageable pageable);
}
