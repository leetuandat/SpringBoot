package com.example.Project.service;

import com.example.Project.dto.TransportMethodDTO;

import java.util.List;

public interface TransportMethodService {
    List<TransportMethodDTO> findAll();
    TransportMethodDTO findById(Long id);
    TransportMethodDTO save(TransportMethodDTO dto);
    TransportMethodDTO update(Long id, TransportMethodDTO dto);
    void delete(Long id);
    List<TransportMethodDTO> findByTransportName(String keyword);
}
