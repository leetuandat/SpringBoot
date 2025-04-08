package com.example.Project.mapper;


import com.example.Project.dto.ProductImageDTO;
import com.example.Project.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageDTO toDto(ProductImage entity);
    ProductImage toEntity(ProductImageDTO dto);

    List<ProductImageDTO> toDtoList(List<ProductImage> entities);
    List<ProductImage> toEntityList(List<ProductImageDTO> dtos);
}
