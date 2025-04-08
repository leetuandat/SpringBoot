package com.example.Project.mapper;

import com.example.Project.dto.CategoryDTO;
import com.example.Project.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    CategoryDTO toDto(Category entity);
    Category toEntity(CategoryDTO dto);

    List<CategoryDTO> toDtoList(List<Category> entities);
    List<Category> toEntityList(List<CategoryDTO> dtos);
}
