/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/12/2025
 * @time: 05:13 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.ProductImageDTO;
import com.example.Project.entity.Product;
import com.example.Project.entity.ProductImage;
import com.example.Project.mapper.ProductImageMapper;
import com.example.Project.repository.ProductImageRepository;
import com.example.Project.repository.ProductRepository;
import com.example.Project.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ProductImageDTO> findAll() {
        return productImageMapper.toDtoList(productImageRepository.findAll());
    }

    @Override
    public ProductImageDTO findById(Long id) {
        ProductImage entity = productImageRepository.findById(id).orElseThrow(() -> new RuntimeException("Product Image Not Found"));
        return productImageMapper.toDto(entity);
    }

    @Override
    public ProductImageDTO save(ProductImageDTO dto) {
        ProductImage entity = productImageMapper.toEntity(dto);
        Product p = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        entity.setProduct(p);
        ProductImage saved = productImageRepository.save(entity);
        return productImageMapper.toDto(saved);
    }

    @Override
    public ProductImageDTO update(Long id, ProductImageDTO dto) {
        ProductImage entity = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product image not found"));
        Product p = productRepository.findById(dto.productId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        entity.setName(dto.name());
        entity.setUrlImg(dto.urlImg());
        entity.setProduct(p);
        ProductImage updated = productImageRepository.save(entity);
        return productImageMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        ProductImage entity = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product image not found"));
        productImageRepository.delete(entity);
    }

    @Override
    public List<ProductImageDTO> findByProductImageName(String keyword) {
        List<ProductImage> images = productImageRepository.findByNameContainingIgnoreCase(keyword);
        return images.stream().map(productImageMapper::toDto).collect(Collectors.toList());
    }
}
