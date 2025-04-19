/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/16/2025
 * @time: 04:36 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.NewsDTO;
import com.example.Project.entity.News;
import com.example.Project.entity.NewsCategory;
import com.example.Project.mapper.NewsMapper;
import com.example.Project.repository.NewsCategoryRepository;
import com.example.Project.repository.NewsRepository;
import com.example.Project.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final NewsCategoryRepository newsCategoryRepository;

    @Override
    public Page<NewsDTO> findAll(Pageable pageable) {
        return (newsRepository.findAll(pageable)).map(newsMapper::toDto);
    }

    @Override
    public NewsDTO findById(Long id) {
        News entity = newsRepository.findById(id).orElseThrow(() -> new RuntimeException("News not found!"));
        return newsMapper.toDto(entity);
    }

    @Override
    public Page<NewsDTO> findByName(String keyword, Pageable pageable) {
        return (newsRepository.findByNameContainingIgnoreCase(keyword, pageable)).map(newsMapper::toDto);
    }

    @Override
    public NewsDTO save(NewsDTO dto) {
        News entity = newsMapper.toEntity(dto);
        entity.setIsDelete(false);
        entity.setIsActive(dto.isActive());

        //set category
        NewsCategory category = newsCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("News category not found!"));
        entity.setNewsCategory(category);

        News saved = newsRepository.save(entity);
        return newsMapper.toDto(saved);
    }

    @Override
    public NewsDTO update(Long id, NewsDTO dto) {
        News entity = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found!"));
        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setImage(dto.image());
        entity.setIsActive(dto.isActive());

        //set Category
        NewsCategory cat = newsCategoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("News category not found!"));
        entity.setNewsCategory(cat);
        News updated = newsRepository.save(entity);
        return newsMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        News entity = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));
        entity.setIsDelete(true);
        newsRepository.save(entity);
    }
}
