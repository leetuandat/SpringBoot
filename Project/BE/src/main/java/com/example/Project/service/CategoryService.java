package com.example.Project.service;

import com.example.Project.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CategoryService {
    Page<CategoryDTO> findAll(Pageable pageable);
    CategoryDTO findById(Long id);
    CategoryDTO save(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
    Page<CategoryDTO> findByCategoryName(String keyword, Pageable pageable);
}
