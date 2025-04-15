/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/12/2025
 * @time: 05:00 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.TransportMethodDTO;
import com.example.Project.entity.TransportMethod;
import com.example.Project.mapper.TransportMethodMapper;
import com.example.Project.repository.TransportMethodRepository;
import com.example.Project.service.TransportMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TransportMethodServiceImpl implements TransportMethodService {

    private final TransportMethodRepository transportMethodRepository;
    private final TransportMethodMapper transportMethodMapper;

    @Override
    public List<TransportMethodDTO> findAll() {
        return transportMethodMapper.toDtoList(transportMethodRepository.findAll());
    }

    @Override
    public TransportMethodDTO findById(Long id) {
        TransportMethod entity = transportMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("TransportMethod not found"));
        return transportMethodMapper.toDto(entity);
    }

    @Override
    public TransportMethodDTO save(TransportMethodDTO dto) {
        TransportMethod entity = transportMethodMapper.toEntity(dto);
        entity.setIsDelete(false);
        return transportMethodMapper.toDto(transportMethodRepository.save(entity));
    }

    @Override
    public TransportMethodDTO update(Long id, TransportMethodDTO dto) {
        TransportMethod entity = transportMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("TransportMethod not found"));
        entity.setName(dto.name());
        entity.setNotes(dto.notes());
        entity.setIsActive(dto.isActive());
        return transportMethodMapper.toDto(transportMethodRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        TransportMethod entity = transportMethodRepository.findById(id).orElseThrow(() -> new RuntimeException("TransportMethod not found"));
        entity.setIsDelete(true);
        transportMethodRepository.save(entity);
    }

    @Override
    public List<TransportMethodDTO> findByTransportName(String keyword) {
        List<TransportMethod> transportMethodList = transportMethodRepository.findByNameContainingIgnoreCase(keyword);
        return transportMethodList.stream().map(transportMethodMapper::toDto).collect(Collectors.toList());
    }
}
