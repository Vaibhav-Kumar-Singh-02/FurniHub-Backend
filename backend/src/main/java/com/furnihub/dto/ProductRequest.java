package com.furnihub.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    private String subcategory;

    private String description;

    private String ingredients;

    private String benefits;

    private String howToUse;

    private String furnitureType;

    private String productSize;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private Integer discount = 0;

    @NotNull(message = "Stock is required")
    private Integer stock;

    @NotBlank(message = "Status is required")
    private String status;

    private List<String> imageUrls;
}