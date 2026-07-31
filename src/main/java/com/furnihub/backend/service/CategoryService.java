package com.furnihub.backend.service;

import com.furnihub.backend.dto.CategoryDto;
import com.furnihub.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllActive() {
        return categoryRepository.findByActiveTrueOrderByNameAsc().stream()
                .map(CategoryDto::from)
                .toList();
    }
}
