package com.example.Project.service;

import com.example.Project.dto.ProductDTO;
import com.example.Project.entity.Product;

import java.util.*;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
    ProductDTO save(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    List<ProductDTO> findByProductName(String keyword);
}
