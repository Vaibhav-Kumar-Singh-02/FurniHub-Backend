package com.furnihub.backend.dto;

import com.furnihub.backend.entity.Category;

public record CategoryDto(Long id, String name, String description, String imageUrl) {

    public static CategoryDto from(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl()
        );
    }
}
