package com.example.Project.service;

import com.example.Project.dto.ProductDTO;
import com.example.Project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface ProductService {
    Page<ProductDTO> findAll(Pageable pageable);
    ProductDTO findById(Long id);
    ProductDTO save(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    List<ProductDTO> findByIds(List<Long> ids);
    Page<ProductDTO> findByProductName(String keyword, Pageable pageable);
    Page<ProductDTO> findByCategoryName(String keyword, Pageable pageable);
}
