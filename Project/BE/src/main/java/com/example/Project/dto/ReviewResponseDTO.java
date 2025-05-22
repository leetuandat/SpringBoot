package com.example.Project.dto;

import java.time.LocalDateTime;

public record ReviewResponseDTO(
   String customerName,
   String customerAvatar,
   int rating,
   String comment,
   LocalDateTime createdAt
) {}
