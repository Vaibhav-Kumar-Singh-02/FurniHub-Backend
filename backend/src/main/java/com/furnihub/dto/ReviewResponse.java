package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Integer reviewId;
    private Integer productId;
    private String productName;
    private Integer userId;
    private String userFullName;
    private Integer rating;
    private String comment;
    private String status;
    private String adminReply;
    private LocalDateTime createdAt;
}