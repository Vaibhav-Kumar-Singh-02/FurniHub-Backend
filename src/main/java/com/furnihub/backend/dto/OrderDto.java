package com.furnihub.backend.dto;

import com.furnihub.backend.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        String orderNumber,
        BigDecimal totalAmount,
        String status,
        String shippingAddress,
        String shippingCity,
        String shippingState,
        String shippingZip,
        String shippingCountry,
        LocalDateTime orderDate,
        List<OrderItemDto> items
) {

    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getOrderNumber(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getShippingAddress(),
                order.getShippingCity(),
                order.getShippingState(),
                order.getShippingZip(),
                order.getShippingCountry(),
                order.getOrderDate(),
                order.getOrderItems().stream().map(OrderItemDto::from).toList()
        );
    }
}
