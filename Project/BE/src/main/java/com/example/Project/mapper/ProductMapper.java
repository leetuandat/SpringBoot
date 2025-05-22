package com.example.Project.mapper;

import com.example.Project.dto.ProductDTO;
import com.example.Project.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "category.name", target = "categoryName")
    ProductDTO toDto(Product entity);

    @Mapping(target = "author", ignore = true) // set ở service
    @Mapping(target = "category", ignore = true) // nếu có
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "contents", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "metaTitle", ignore = true)
    @Mapping(target = "metaKeyword", ignore = true)
    @Mapping(target = "metaDescription", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDelete", constant = "false")
    Product toEntity(ProductDTO dto);

    List<Product> toEntityList(List<ProductDTO> dtos);
    List<ProductDTO> toDtoList(List<Product> entities);
}
