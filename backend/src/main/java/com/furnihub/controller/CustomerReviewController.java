package com.furnihub.controller;

import com.furnihub.dto.ReviewRequest;
import com.furnihub.dto.ReviewResponse;
import com.furnihub.entity.Product;
import com.furnihub.entity.Review;
import com.furnihub.entity.User;
import com.furnihub.repository.ProductRepository;
import com.furnihub.repository.ReviewRepository;
import com.furnihub.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer/reviews")
public class CustomerReviewController {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CustomerReviewController(ReviewRepository reviewRepository,
                                    ProductRepository productRepository,
                                    UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Integer productId) {
        List<Review> reviews = reviewRepository.findByProduct_ProductIdOrderByCreatedAtDesc(productId);
        List<ReviewResponse> response = reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Review> reviews = reviewRepository.findByUser_UserIdOrderByCreatedAtDesc(user.getUserId());
        List<ReviewResponse> response = reviews.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> submitReview(Authentication authentication,
                                          @RequestBody ReviewRequest request) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Review review = new Review();
            review.setProduct(product);
            review.setUser(user);
            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setStatus(Review.ReviewStatus.PENDING);

            reviewRepository.save(review);

            ReviewResponse response = mapToResponse(review);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to submit review: " + e.getMessage());
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer reviewId, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).body("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
        return ResponseEntity.ok("Review deleted successfully");
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getReviewId(),
                review.getProduct().getProductId(),
                review.getProduct().getName(),
                review.getUser().getUserId(),
                review.getUser().getFullName(),
                review.getRating(),
                review.getComment(),
                review.getStatus().name(),
                review.getAdminReply(),
                review.getCreatedAt()
        );
    }
}
