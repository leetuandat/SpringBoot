package com.example.Project.repository;

import com.example.Project.entity.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Page<Configuration> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
