package com.example.Project.mapper;

import com.example.Project.dto.TransportMethodDTO;
import com.example.Project.entity.TransportMethod;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface TransportMethodMapper {

    TransportMethodDTO toDto(TransportMethod entity);
    TransportMethod toEntity(TransportMethodDTO dto);

    List<TransportMethodDTO> toDtoList(List<TransportMethod> entity);
    List<TransportMethod> toEntityList(List<TransportMethodDTO> dto);
}
