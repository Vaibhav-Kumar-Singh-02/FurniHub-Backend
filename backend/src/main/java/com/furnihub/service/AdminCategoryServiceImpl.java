package com.furnihub.service;

import com.furnihub.dto.CategoryRequest;
import com.furnihub.dto.CategoryResponse;
import com.furnihub.entity.Category;
import com.furnihub.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {

    private static final Logger logger = LoggerFactory.getLogger(AdminCategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    public AdminCategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponse addCategory(CategoryRequest request) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        category = categoryRepository.save(category);

        logger.info("Category added successfully with id: {}", category.getCategorieId());
        CategoryResponse response = new CategoryResponse();
        response.setCategorieId(category.getCategorieId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());
        return response;
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());

        category = categoryRepository.save(category);

        logger.info("Category updated successfully with id: {}", id);
        CategoryResponse response = new CategoryResponse();
        response.setCategorieId(category.getCategorieId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());
        return response;
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        categoryRepository.delete(category);
        logger.info("Category deleted successfully with id: {}", id);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> {
                    CategoryResponse response = new CategoryResponse();
                    response.setCategorieId(category.getCategorieId());
                    response.setCategoryName(category.getCategoryName());
                    return response;
                })
                .collect(Collectors.toList());
    }
}