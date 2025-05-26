/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/23/2025
 * @time: 05:24 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.ReviewDTO;
import com.example.Project.dto.ReviewResponseDTO;
import com.example.Project.entity.ProductReview;
import com.example.Project.mapper.ReviewMapper;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.ProductRepository;
import com.example.Project.repository.ProductReviewRepository;
import com.example.Project.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void createReview(Long productId, Long customerId, ReviewDTO dto) {
        ProductReview review = new ProductReview();
        review.setProduct(productRepository.findById(productId).orElseThrow(() ->  new RuntimeException("Không tìm thấy sản phẩm")));
        review.setCustomer(customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng")));
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setCreatedAt(LocalDateTime.now());
        productReviewRepository.save(review);
    }

    @Override
    public List<ReviewResponseDTO> getReviewsByProduct(Long productId) {
        List<ProductReview> reviews = productReviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(ReviewMapper::toDTO)
                .toList();
    }
}
