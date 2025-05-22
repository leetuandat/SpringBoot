/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/17/2025
 * @time: 09:40 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.ReviewDTO;
import com.example.Project.dto.ReviewResponseDTO;
import com.example.Project.entity.Customer;
import com.example.Project.entity.Product;
import com.example.Project.entity.ProductReview;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.repository.ProductRepository;
import com.example.Project.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor

public class ReviewController {

    private final ProductReviewRepository reviewRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    @PostMapping
    public String createReview(@RequestBody ReviewDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        Customer customer = customerRepo.findByUsername(userDetails.getUsername()).orElseThrow();
        Product product = productRepo.findById(dto.productId()).orElseThrow();

        ProductReview review = new ProductReview();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.rating());
        review.setComment(dto.comment());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepo.save(review);

        return "Đánh giá thành công!";
    }

    @GetMapping("/{productId}")
    public List<ReviewResponseDTO> getReviews(@PathVariable Long productId) {
        return reviewRepo.findByProductId(productId).stream()
                .map(review -> new ReviewResponseDTO(
                        review.getCustomer().getName(),
                        review.getCustomer().getAvatar(),
                        review.getRating(),
                        review.getComment(),
                        review.getCreatedAt()
                )).toList();
    }

}
