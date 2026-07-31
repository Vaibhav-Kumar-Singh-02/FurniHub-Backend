package com.furnihub.backend.dto;

import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.ProductImage;

import java.math.BigDecimal;
import java.util.List;

public record ProductImageDto(Long id, String imageUrl, boolean isPrimary, int displayOrder) {

    public static ProductImageDto from(ProductImage image) {
        return new ProductImageDto(
                image.getId(),
                image.getImageUrl(),
                image.isPrimary(),
                image.getDisplayOrder()
        );
    }
}
