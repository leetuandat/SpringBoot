package com.example.Project.repository;

import com.example.Project.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductId(Long productId);
    Optional<ProductReview> findByProductIdAndCustomerId(Long productId, Long customerId);
}
