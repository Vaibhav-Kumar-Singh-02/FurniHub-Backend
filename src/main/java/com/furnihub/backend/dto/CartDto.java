package com.furnihub.backend.dto;

import com.furnihub.backend.entity.CartItem;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        List<CartItemDto> items,
        int totalItems,
        BigDecimal totalAmount
) {

    public static CartDto from(List<CartItem> cartItems) {
        List<CartItemDto> items = cartItems.stream().map(CartItemDto::from).toList();
        int totalItems = items.stream().mapToInt(CartItemDto::quantity).sum();
        BigDecimal totalAmount = items.stream()
                .map(CartItemDto::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartDto(items, totalItems, totalAmount);
    }
}
