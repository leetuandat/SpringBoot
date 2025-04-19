package com.example.Project.repository;

import com.example.Project.entity.TransportMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportMethodRepository extends JpaRepository<TransportMethod, Long> {
    Page<TransportMethod> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
