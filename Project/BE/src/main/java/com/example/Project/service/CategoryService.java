package com.example.Project.service;

import com.example.Project.dto.CategoryDTO;

import java.util.List;


public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    CategoryDTO save(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
    List<CategoryDTO> findByCategoryName(String keyword);
}
