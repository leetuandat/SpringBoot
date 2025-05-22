package com.example.Project.dto;

public record ReviewDTO(
        Long productId,
        int rating,
        String comment
) {}
