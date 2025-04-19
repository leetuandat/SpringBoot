package com.example.Project.service;

import com.example.Project.dto.TransportMethodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransportMethodService {
    Page<TransportMethodDTO> findAll(Pageable pageable);
    TransportMethodDTO findById(Long id);
    TransportMethodDTO save(TransportMethodDTO dto);
    TransportMethodDTO update(Long id, TransportMethodDTO dto);
    void delete(Long id);
    Page<TransportMethodDTO> findByTransportName(String keyword, Pageable pageable);
}
