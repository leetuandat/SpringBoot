package com.example.Project.service;

import com.example.Project.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {
    Page<NewsDTO> findAll(Pageable pageable);
    NewsDTO findById(Long id);
    NewsDTO save(NewsDTO dto);
    NewsDTO update(Long id, NewsDTO dto);
    void deleteById(Long id);
    Page<NewsDTO> findByName(String keyword, Pageable pageable);
}
