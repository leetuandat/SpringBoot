package com.example.Project.mapper;

import com.example.Project.dto.ProductDTO;
import com.example.Project.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product entity);
    Product toEntity(ProductDTO dto);

    List<Product> toEntityList(List<ProductDTO> dtos);
    List<ProductDTO> toDtoList(List<Product> entities);
}
