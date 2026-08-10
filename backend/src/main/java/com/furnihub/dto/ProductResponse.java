package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Integer productId;
    private String name;
    private String brand;
    private String categoryName;
    private String subcategory;
    private String description;
    private String ingredients;
    private String benefits;
    private String howToUse;
    private String furnitureType;
    private String productSize;
    private BigDecimal price;
    private Integer discount;
    private Integer stock;
    private Double ratings;
    private String status;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}