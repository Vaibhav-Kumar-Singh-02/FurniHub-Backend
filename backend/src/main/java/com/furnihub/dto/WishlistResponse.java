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
public class WishlistResponse {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String productName;
    private String brand;
    private BigDecimal price;
    private Integer discount;
    private Integer stock;
    private String categoryName;
    private List<String> imageUrls;
    private LocalDateTime createdAt;
}
