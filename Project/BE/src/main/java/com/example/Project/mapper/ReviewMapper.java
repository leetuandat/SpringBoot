package com.example.Project.mapper;

import com.example.Project.dto.ReviewResponseDTO;
import com.example.Project.entity.ProductReview;

public interface ReviewMapper {
    public static ReviewResponseDTO toDTO(ProductReview entity) {
        return new ReviewResponseDTO(
                entity.getCustomer().getName(),
                entity.getCustomer().getAvatar(),
                entity.getRating(),
                entity.getComment(),
                entity.getCreatedAt()
        );
    }
}
