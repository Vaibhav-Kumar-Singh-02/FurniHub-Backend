package com.furnihub.controller;

import com.furnihub.dto.ReviewActionRequest;
import com.furnihub.dto.ReviewResponse;
import com.furnihub.enums.ReviewStatus;
import com.furnihub.service.AdminReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllReviews(@RequestParam(required = false) String status,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        int adjustedPage = Math.max(0, page - 1);
        Page<ReviewResponse> reviewPage = adminReviewService.getAllReviews(status, adjustedPage, size);
        return ResponseEntity.ok(Map.of(
                "reviews", reviewPage.getContent(),
                "totalPages", reviewPage.getTotalPages()
        ));
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Integer reviewId) {
        ReviewResponse response = adminReviewService.getReviewById(reviewId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reviewId}/approve")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Integer reviewId) {
        ReviewResponse response = adminReviewService.approveReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reviewId}/reject")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Integer reviewId) {
        ReviewResponse response = adminReviewService.rejectReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        adminReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{reviewId}/reply")
    public ResponseEntity<ReviewResponse> replyToReview(@PathVariable Integer reviewId, @Valid @RequestBody ReviewActionRequest request) {
        ReviewResponse response = adminReviewService.replyToReview(reviewId, request);
        return ResponseEntity.ok(response);
    }
}
