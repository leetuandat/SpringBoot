/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/15/2025
 * @time: 05:57 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.NewsCategoryDTO;
import com.example.Project.entity.NewsCategory;
import com.example.Project.mapper.NewsCategoryMapper;
import com.example.Project.repository.CategoryRepository;
import com.example.Project.repository.NewsCategoryRepository;
import com.example.Project.service.NewCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewCategoryServiceImpl implements NewCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;
    private final NewsCategoryMapper newsCategoryMapper;

    @Override
    public Page<NewsCategoryDTO> findAll(Pageable pageable) {
        return (newsCategoryRepository.findAll(pageable)).map(newsCategoryMapper::toDto);
    }

    @Override
    public NewsCategoryDTO findById(Long id) {
        NewsCategory entity = newsCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("News category not found!"));
        return newsCategoryMapper.toDto(entity);
    }

    @Override
    public NewsCategoryDTO save(NewsCategoryDTO dto) {
        NewsCategory entity = newsCategoryMapper.toEntity(dto);
        entity.setIsDelete(false);
        return newsCategoryMapper.toDto(newsCategoryRepository.save(entity));
    }

    @Override
    public NewsCategoryDTO update(Long id, NewsCategoryDTO dto) {
        NewsCategory entity = newsCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("News category not found!"));
        entity.setName(dto.name());
        entity.setIcon(dto.icon());
        entity.setSlug(dto.slug());
        entity.setIsActive(dto.isActive());
        NewsCategory updated = newsCategoryRepository.save(entity);
        return newsCategoryMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        NewsCategory entity = newsCategoryRepository.findById(id).orElseThrow(() -> new RuntimeException("News category not found!"));
        entity.setIsDelete(true);
        newsCategoryRepository.save(entity);
    }

    @Override
    public Page<NewsCategoryDTO> findByName(String keyword, Pageable pageable) {
        Page<NewsCategory> newsCategoryList = newsCategoryRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return newsCategoryList.map(newsCategoryMapper::toDto);
    }
}
