package com.example.Project.service;

import com.example.Project.dto.ConfigurationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ConfigurationService {
    Page<ConfigurationDTO> findAll(Pageable pageable);
    ConfigurationDTO findById(Long id);
    ConfigurationDTO save(ConfigurationDTO dto);
    ConfigurationDTO update(Long id, ConfigurationDTO dto);
    void deleteById(Long id);
    Page<ConfigurationDTO> findByName(String keyword, Pageable pageable);
}
