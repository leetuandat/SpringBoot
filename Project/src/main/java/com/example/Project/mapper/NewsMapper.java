package com.example.Project.mapper;


import com.example.Project.dto.NewsDTO;
import com.example.Project.entity.News;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsDTO toDto(News entity);
    News toEntity(NewsDTO dto);
    List<NewsDTO> toDtoList(List<News> entities);
    List<News> toEntityList(List<NewsDTO> dtos);
}
