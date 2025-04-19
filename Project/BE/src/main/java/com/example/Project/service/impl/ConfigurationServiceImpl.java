/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/15/2025
 * @time: 06:17 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.ConfigurationDTO;
import com.example.Project.entity.Configuration;
import com.example.Project.mapper.ConfigurationMapper;
import com.example.Project.repository.ConfigurationRepository;
import com.example.Project.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    @Override
    public Page<ConfigurationDTO> findAll(Pageable pageable) {
        return (configurationRepository.findAll(pageable)).map(configurationMapper::toDto);
    }

    @Override
    public ConfigurationDTO findById(Long id) {
        Configuration entity = configurationRepository.findById(id).orElseThrow(() -> new RuntimeException("Configuration not found"));
        return configurationMapper.toDto(entity);
    }

    @Override
    public ConfigurationDTO save(ConfigurationDTO dto) {
        Configuration entity = configurationMapper.toEntity(dto);
        entity.setIsDelete(false);
        return configurationMapper.toDto(configurationRepository.save(entity));
    }

    @Override
    public ConfigurationDTO update(Long id, ConfigurationDTO dto) {
        Configuration entity = configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
        entity.setName(dto.name());
        entity.setIsActive(dto.isActive());
        Configuration updated = configurationRepository.save(entity);
        return configurationMapper.toDto(updated);
    }

    @Override
    public void deleteById(Long id) {
        Configuration entity = configurationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
        entity.setIsDelete(true);
        configurationRepository.save(entity);
    }

    @Override
    public Page<ConfigurationDTO> findByName(String keyword, Pageable pageable) {
        Page<Configuration> configurationList = configurationRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return configurationList.map(configurationMapper::toDto);
    }
}
