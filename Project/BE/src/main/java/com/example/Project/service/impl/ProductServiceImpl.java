/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 03:39 AM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.ProductDTO;
import com.example.Project.entity.Product;
import com.example.Project.mapper.ProductMapper;
import com.example.Project.repository.ProductRepository;
import com.example.Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductDTO> findAll(Pageable pageable) {
        return (productRepository.findAll(pageable)).map(productMapper::toDto);
    }

    @Override
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toDto(entity);
    }

    @Override
    public ProductDTO save(ProductDTO dto) {
        Product entity = productMapper.toEntity(dto);
        entity.setIsDelete(false);
        Product saved = productRepository.save(entity);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        // cập nhật trường tuỳ ý
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());
        entity.setImage(dto.image());
        entity.setIsActive(dto.isActive());
        Product updated = productRepository.save(entity);
        return productMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        entity.setIsDelete(true);
        productRepository.save(entity);
    }

    @Override
    public Page<ProductDTO> findByProductName(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return products.map(productMapper::toDto);
    }
}
