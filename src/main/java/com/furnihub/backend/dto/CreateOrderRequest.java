package com.furnihub.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrderRequest(
        @NotBlank(message = "Shipping address is required")
        String shippingAddress,

        @NotBlank(message = "Shipping city is required")
        @Size(max = 255, message = "Shipping city must be at most 255 characters")
        String shippingCity,

        @Size(max = 255, message = "Shipping state must be at most 255 characters")
        String shippingState,

        @Size(max = 20, message = "Shipping zip must be at most 20 characters")
        String shippingZip,

        @NotBlank(message = "Shipping country is required")
        @Size(max = 255, message = "Shipping country must be at most 255 characters")
        String shippingCountry
) {
}
