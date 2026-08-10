package com.furnihub.service;

import com.furnihub.dto.ProductRequest;
import com.furnihub.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminProductService {

    ProductResponse addProduct(ProductRequest request);

    ProductResponse updateProduct(Integer id, ProductRequest request);

    void deleteProduct(Integer id);

    Page<ProductResponse> getAllProducts(String search, String category, String status, int page, int size);

    ProductResponse getProductById(Integer id);

    List<ProductResponse> searchProducts(String keyword);
}