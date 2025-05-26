/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 02:59 AM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.CategoryDTO;
import com.example.Project.entity.Category;
import com.example.Project.mapper.CategoryMapper;
import com.example.Project.repository.CategoryRepository;
import com.example.Project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.map(categoryMapper::toDto);
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDTO save(CategoryDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        category.setIsDelete(false);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        //Map cac truong tu dto
        category.setName(dto.name());
        category.setIcon(dto.icon());
        category.setSlug(dto.slug());
        category.setIsActive(dto.isActive());
        Category updated = categoryRepository.save(category);
        return categoryMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryDTO> findByCategoryName(String keyword, Pageable pageable) {
        Page<Category> categories = categoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return categories.map(categoryMapper::toDto);
    }
}
