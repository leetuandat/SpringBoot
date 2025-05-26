package com.example.Project.service;

import com.example.Project.dto.ReviewDTO;
import com.example.Project.dto.ReviewResponseDTO;

import java.util.List;

public interface ProductReviewService {
    void createReview(Long productId, Long customerId, ReviewDTO dto);
    List<ReviewResponseDTO> getReviewsByProduct(Long productId);
}
