package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private Integer orderItemId;
    private String orderId;
    private Integer productId;
    private String productName;
    private String brand;
    private Integer quantity;
    private BigDecimal pricePerUnit;
    private BigDecimal totalPrice;
}