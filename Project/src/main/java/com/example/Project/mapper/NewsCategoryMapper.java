package com.example.Project.mapper;

import com.example.Project.dto.NewsCategoryDTO;
import com.example.Project.entity.NewsCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsCategoryMapper {
    NewsCategoryDTO toDto(NewsCategory entity);
    NewsCategory toEntity(NewsCategoryDTO dto);
    List<NewsCategoryDTO> toDtoList(List<NewsCategory> entities);
    List<NewsCategory> toEntityList(List<NewsCategoryDTO> dtos);
}
