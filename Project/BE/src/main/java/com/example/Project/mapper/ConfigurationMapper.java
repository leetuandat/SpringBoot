package com.example.Project.mapper;

import com.example.Project.dto.ConfigurationDTO;
import com.example.Project.entity.Configuration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
    ConfigurationDTO toDto (Configuration entity);
    Configuration toEntity (ConfigurationDTO dto);

    List<ConfigurationDTO> toDtoList (List<Configuration> entities);
    List<Configuration> toEntityList (List<ConfigurationDTO> dtoList);
}
