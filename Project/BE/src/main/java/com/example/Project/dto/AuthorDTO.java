package com.example.Project.dto;

import java.util.List;

public record AuthorDTO(
        Long id,
        String name,
        String authorKey,
        String biography,
        String image,
        List<String> bookTitles
) {}
