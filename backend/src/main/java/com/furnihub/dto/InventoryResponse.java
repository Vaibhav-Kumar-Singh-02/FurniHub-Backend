package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Integer productId;
    private String name;
    private Integer stock;
    private String categoryName;
    private String status;
    private Boolean lowStock;
}