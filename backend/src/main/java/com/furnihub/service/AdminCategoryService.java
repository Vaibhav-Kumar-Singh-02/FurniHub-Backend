package com.furnihub.service;

import com.furnihub.dto.CategoryRequest;
import com.furnihub.dto.CategoryResponse;

import java.util.List;

public interface AdminCategoryService {

    CategoryResponse addCategory(CategoryRequest request);

    CategoryResponse updateCategory(Integer id, CategoryRequest request);

    void deleteCategory(Integer id);

    List<CategoryResponse> getAllCategories();
}