package com.furnihub.repository;

import com.furnihub.entity.Review;
import com.furnihub.entity.Review.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProduct_ProductIdOrderByCreatedAtDesc(Integer productId);
    List<Review> findByStatusOrderByCreatedAtDesc(ReviewStatus status);
    Page<Review> findByStatus(ReviewStatus status, Pageable pageable);
    long countByProduct_ProductIdAndStatus(Integer productId, ReviewStatus status);
    List<Review> findByUser_UserIdOrderByCreatedAtDesc(Integer userId);
}