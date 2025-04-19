package com.example.Project.service;

import com.example.Project.dto.ProductConfigDTO;
import com.example.Project.entity.ProductConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductConfigService {
    Page<ProductConfigDTO> findAll(Pageable pageable);
    ProductConfigDTO findById(Long id);
    List<ProductConfigDTO> findByProductId(Long productId);
    List<ProductConfigDTO> findByConfigurationId(Long configId);
    ProductConfigDTO save(ProductConfigDTO dto);
    ProductConfigDTO update(Long id, ProductConfigDTO dto);
    void deleteById(Long id);
}
