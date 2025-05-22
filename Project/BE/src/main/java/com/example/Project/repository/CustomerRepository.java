package com.example.Project.repository;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Page<Customer> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Optional<Customer> findByUsername(String username);

}
