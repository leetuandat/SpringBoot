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
import com.example.Project.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createOrUpdateReview(@RequestBody ReviewDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn phải đăng nhập để đánh giá.");
        }

        String username = auth.getName();
        Customer customer = customerRepo.findByUsername(username).orElseThrow();
        Product product = productRepo.findById(dto.productId()).orElseThrow();

        // Tìm review đã tồn tại của user này cho product này
        ProductReview review = reviewRepo.findByProductIdAndCustomerId(product.getId(), customer.getId())
                .orElse(new ProductReview());

        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.rating());
        review.setComment(dto.comment());

        if (review.getId() == null) {
            review.setCreatedAt(LocalDateTime.now());
        }
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepo.save(review);

        return ResponseEntity.ok("Đánh giá đã được lưu!");
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
