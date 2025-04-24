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
    @Mapping(target = "bookTitles", expression = "java(mapBookTitles(author))")
    AuthorDTO toDTO(Author author);

    // Đảo chiều không set bookTitles vì đó là thông tin tham chiếu (không có trong DTO input)
    @InheritInverseConfiguration
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorDTO dto);

    // Hàm custom để trích xuất tên sách từ danh sách Product
    default List<String> mapBookTitles(Author author) {
        if (author.getBooks() == null) return null;
        return author.getBooks().stream()
                .map(book -> book.getName())
                .collect(Collectors.toList());
    }
}
