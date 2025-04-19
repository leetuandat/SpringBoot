package com.example.Project.service;

import com.example.Project.dto.NewsCategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewCategoryService {
    Page<NewsCategoryDTO> findAll(Pageable pageable);
    NewsCategoryDTO findById(Long id);
    NewsCategoryDTO save(NewsCategoryDTO dto);
    NewsCategoryDTO update(Long id, NewsCategoryDTO dto);
    void delete(Long id);
    Page<NewsCategoryDTO> findByName(String keyword, Pageable pageable);
}
