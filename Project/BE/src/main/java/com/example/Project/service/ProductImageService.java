package com.example.Project.service;

import com.example.Project.dto.ProductImageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductImageService {
    Page<ProductImageDTO> findAll(Pageable pageable);
    ProductImageDTO findById(Long id);
    ProductImageDTO save(ProductImageDTO dto);
    ProductImageDTO update(Long id, ProductImageDTO dto);
    void delete(Long id);
    Page<ProductImageDTO> findByProductImageName(String keyword, Pageable pageable);
}
