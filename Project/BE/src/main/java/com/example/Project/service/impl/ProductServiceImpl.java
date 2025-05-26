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
import com.example.Project.entity.Category;
import com.example.Project.entity.Product;
import com.example.Project.mapper.ProductMapper;
import com.example.Project.repository.AuthorRepository;
import com.example.Project.repository.CategoryRepository;
import com.example.Project.repository.ProductRepository;
import com.example.Project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

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
    @Transactional
    public ProductDTO save(ProductDTO dto) {
        Product entity;

        if (dto.id() != null) {
            // → update: chỉ tìm khi có id
            entity = productRepository.findById(dto.id())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Product không tồn tại id=" + dto.id())
                    );
        } else {
            // → create mới: không gọi findById
            entity = new Product();
        }

        // Map các trường từ dto vào entity (dù create hay update)
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setQuantity(dto.quantity());
        entity.setImage(dto.image());
        entity.setIsActive(dto.isActive());
        // … các trường khác …

        // Thiết lập quan hệ Category
        Category cat = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy thể loại id=" + dto.categoryId())
                );
        entity.setCategory(cat);

        // Lưu entity (insert hoặc update tuỳ trường hợp)
        Product saved = productRepository.save(entity);
        return productMapper.toDto(saved);
    }


    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại id=" + id));

        // Cập nhật các trường cơ bản
        productMapper.updateEntityFromDto(dto, entity);

        // Nếu client đổi categoryId, cập nhật lại quan hệ
        if (!entity.getCategory().getId().equals(dto.categoryId())) {
            Category cat = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Không tìm thấy thể loại id=" + dto.categoryId())
                    );
            entity.setCategory(cat);
        }

        Product updated = productRepository.save(entity);
        return productMapper.toDto(updated);
    }


    @Override
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> findByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProductDTO> findByProductName(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return products.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDTO> findByCategoryName(String keyword, Pageable pageable) {
        Page<Product> products = productRepository.findByCategory_NameIgnoreCase(keyword, pageable);
        return products.map(productMapper::toDto); 
    }
}
