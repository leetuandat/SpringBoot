package com.example.Project.mapper;

import com.example.Project.dto.ProductConfigDTO;
import com.example.Project.entity.ProductConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductConfigMapper {
    ProductConfigDTO toDto(ProductConfig entity);
    ProductConfig toEntity(ProductConfigDTO dto);

    List<ProductConfigDTO> toDtoList(List<ProductConfig> entities);
    List<ProductConfig> toEntityList(List<ProductConfigDTO> dtos);
}
