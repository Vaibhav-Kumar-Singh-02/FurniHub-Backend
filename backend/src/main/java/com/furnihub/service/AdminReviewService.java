package com.furnihub.service;

import com.furnihub.dto.ReviewActionRequest;
import com.furnihub.dto.ReviewResponse;
import org.springframework.data.domain.Page;

public interface AdminReviewService {

    Page<ReviewResponse> getAllReviews(String status, int page, int size);

    ReviewResponse getReviewById(Integer reviewId);

    ReviewResponse approveReview(Integer reviewId);

    ReviewResponse rejectReview(Integer reviewId);

    void deleteReview(Integer reviewId);

    ReviewResponse replyToReview(Integer reviewId, ReviewActionRequest request);
}