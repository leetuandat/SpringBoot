package com.example.Project.repository;

import com.example.Project.entity.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Page<ProductImage> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
