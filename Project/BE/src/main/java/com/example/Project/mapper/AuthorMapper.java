package com.example.Project.mapper;

import com.example.Project.dto.AuthorDTO;
import com.example.Project.entity.Author;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AuthorMapper {
    AuthorDTO toDTO(Author author);

    @InheritInverseConfiguration
    Author toEntity(AuthorDTO dto);
}
