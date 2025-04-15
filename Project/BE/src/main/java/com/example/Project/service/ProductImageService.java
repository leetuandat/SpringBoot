package com.example.Project.service;

import com.example.Project.dto.ProductImageDTO;

import java.util.List;

public interface ProductImageService {
    List<ProductImageDTO> findAll();
    ProductImageDTO findById(Long id);
    ProductImageDTO save(ProductImageDTO dto);
    ProductImageDTO update(Long id, ProductImageDTO dto);
    void delete(Long id);
    List<ProductImageDTO> findByProductImageName(String keyword);
}
