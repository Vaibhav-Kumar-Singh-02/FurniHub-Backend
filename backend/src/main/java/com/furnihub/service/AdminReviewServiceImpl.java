package com.furnihub.service;

import com.furnihub.dto.ReviewActionRequest;
import com.furnihub.dto.ReviewResponse;
import com.furnihub.entity.Product;
import com.furnihub.entity.Review;
import com.furnihub.entity.User;
import com.furnihub.repository.ProductRepository;
import com.furnihub.repository.ReviewRepository;
import com.furnihub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminReviewServiceImpl implements AdminReviewService {

    private static final Logger logger = LoggerFactory.getLogger(AdminReviewServiceImpl.class);

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AdminReviewServiceImpl(ReviewRepository reviewRepository,
                                       ProductRepository productRepository,
                                       UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<ReviewResponse> getAllReviews(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (status != null && !status.isBlank()) {
            Review.ReviewStatus reviewStatus = Review.ReviewStatus.valueOf(status);
            return reviewRepository.findByStatus(reviewStatus, pageable).map(this::mapToResponse);
        }

        return reviewRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    public ReviewResponse getReviewById(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        return mapToResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse approveReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        review.setStatus(Review.ReviewStatus.APPROVED);
        review = reviewRepository.save(review);

        logger.info("Review approved with id: {}", reviewId);
        return mapToResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse rejectReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        review.setStatus(Review.ReviewStatus.REJECTED);
        review = reviewRepository.save(review);

        logger.info("Review rejected with id: {}", reviewId);
        return mapToResponse(review);
    }

    @Override
    @Transactional
    public void deleteReview(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        reviewRepository.delete(review);
        logger.info("Review deleted with id: {}", reviewId);
    }

    @Override
    @Transactional
    public ReviewResponse replyToReview(Integer reviewId, ReviewActionRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        review.setAdminReply(request.getAdminReply());
        review = reviewRepository.save(review);

        logger.info("Admin reply added to review id: {}", reviewId);
        return mapToResponse(review);
    }

    private ReviewResponse mapToResponse(Review review) {
        Product product = productRepository.findById(review.getProduct().getProductId()).orElse(null);
        User user = userRepository.findById(review.getUser().getUserId()).orElse(null);

        return new ReviewResponse(
                review.getReviewId(),
                product != null ? product.getProductId() : null,
                product != null ? product.getName() : null,
                user != null ? user.getUserId() : null,
                user != null ? user.getFullName() : null,
                review.getRating(),
                review.getComment(),
                review.getStatus().name(),
                review.getAdminReply(),
                review.getCreatedAt()
        );
    }
}