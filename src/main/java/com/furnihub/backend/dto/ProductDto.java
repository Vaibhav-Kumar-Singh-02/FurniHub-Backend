package com.furnihub.backend.dto;

import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.ProductImage;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal discountPrice,
        int stockQuantity,
        String sku,
        Long categoryId,
        String categoryName,
        String primaryImageUrl
) {

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscountPrice(),
                product.getStockQuantity(),
                product.getSku(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                primaryImageUrl(product)
        );
    }

    private static String primaryImageUrl(Product product) {
        return product.getImages().stream()
                .filter(ProductImage::isPrimary)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElseGet(() -> product.getImages().stream()
                        .findFirst()
                        .map(ProductImage::getImageUrl)
                        .orElse(null));
    }
}
