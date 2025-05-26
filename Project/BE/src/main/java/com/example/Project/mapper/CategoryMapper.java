package com.example.Project.mapper;

import com.example.Project.dto.CategoryDTO;
import com.example.Project.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryDTO toDto(Category entity);

    @Mapping(target="id",            source="id")
    @Mapping(target="name",          source="name")
    @Mapping(target="icon",          source="icon")
    @Mapping(target="slug",          source="slug")
    @Mapping(target="isActive",      source="isActive")
    @Mapping(target="metaDescription", source="metaDescription")
    Category toEntity(CategoryDTO dto);

    List<CategoryDTO> toDtoList(List<Category> entities);
    List<Category> toEntityList(List<CategoryDTO> dtos);
}
