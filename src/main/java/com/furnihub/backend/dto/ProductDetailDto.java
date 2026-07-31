package com.furnihub.backend.dto;

import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.ProductImage;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public record ProductDetailDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        BigDecimal discountPrice,
        int stockQuantity,
        String sku,
        Long categoryId,
        String categoryName,
        List<ProductImageDto> images
) {

    public static ProductDetailDto from(Product product) {
        List<ProductImageDto> images = product.getImages().stream()
                .sorted(Comparator.comparingInt(ProductImage::getDisplayOrder))
                .map(ProductImageDto::from)
                .toList();
        return new ProductDetailDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscountPrice(),
                product.getStockQuantity(),
                product.getSku(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                images
        );
    }
}
