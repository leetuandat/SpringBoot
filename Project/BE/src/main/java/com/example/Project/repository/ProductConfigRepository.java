package com.example.Project.repository;

import com.example.Project.entity.ProductConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductConfigRepository extends JpaRepository<ProductConfig, Long> {
    List<ProductConfig> findByProduct_Id(Long productId);
    List<ProductConfig> findByConfiguration_Id(Long configId);
}
