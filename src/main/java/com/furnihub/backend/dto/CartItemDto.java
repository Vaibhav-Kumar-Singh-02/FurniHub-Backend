package com.furnihub.backend.dto;

import com.furnihub.backend.entity.CartItem;
import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.ProductImage;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        Long productId,
        String productName,
        String productSku,
        BigDecimal price,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal,
        String imageUrl
) {

    public static CartItemDto from(CartItem item) {
        Product product = item.getProduct();
        BigDecimal unitPrice = product.getDiscountPrice() != null
                ? product.getDiscountPrice()
                : product.getPrice();
        return new CartItemDto(
                item.getId(),
                product.getId(),
                product.getName(),
                product.getSku(),
                product.getPrice(),
                unitPrice,
                item.getQuantity(),
                unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())),
                product.getImages().stream()
                        .filter(ProductImage::isPrimary)
                        .findFirst()
                        .map(ProductImage::getImageUrl)
                        .orElseGet(() -> product.getImages().stream()
                                .findFirst()
                                .map(ProductImage::getImageUrl)
                                .orElse(null))
        );
    }
}
