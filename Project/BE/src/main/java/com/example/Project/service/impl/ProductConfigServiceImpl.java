/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/16/2025
 * @time: 12:39 AM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.ProductConfigDTO;
import com.example.Project.entity.ProductConfig;
import com.example.Project.mapper.ProductConfigMapper;
import com.example.Project.repository.ProductConfigRepository;
import com.example.Project.service.ProductConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductConfigServiceImpl implements ProductConfigService {

    private final ProductConfigRepository productConfigRepository;
    private final ProductConfigMapper productConfigMapper;

    @Override
    public Page<ProductConfigDTO> findAll(Pageable pageable) {
        return (productConfigRepository.findAll(pageable)).map(productConfigMapper::toDto);
    }

    @Override
    public ProductConfigDTO findById(Long id) {
        ProductConfig entity = productConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductConfig not found"));
        return productConfigMapper.toDto(entity);
    }

    @Override
    public List<ProductConfigDTO> findByProductId(Long productId) {
        return productConfigMapper.toDtoList((productConfigRepository.findByProduct_Id(productId)));
    }

    @Override
    public List<ProductConfigDTO> findByConfigurationId(Long configId) {
        return productConfigMapper.toDtoList((productConfigRepository.findByConfiguration_Id(configId)));
    }

    @Override
    public ProductConfigDTO save(ProductConfigDTO dto) {
        ProductConfig entity = productConfigMapper.toEntity(dto);
        ProductConfig saved = productConfigRepository.save(entity);
        return productConfigMapper.toDto(saved);
    }

    @Override
    public ProductConfigDTO update(Long id, ProductConfigDTO dto) {
        ProductConfig existing = productConfigRepository.findById(id).orElseThrow(() -> new RuntimeException("ProductConfig not found"));
        existing.setValue(dto.value());
        ProductConfig updated = productConfigRepository.save(existing);
        return productConfigMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        if (!productConfigRepository.existsById(id)) {
            throw new RuntimeException("ProductConfig not found");
        }
        productConfigRepository.deleteById(id);
    }
}
