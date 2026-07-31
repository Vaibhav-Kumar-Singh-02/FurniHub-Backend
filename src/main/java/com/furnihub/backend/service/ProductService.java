package com.furnihub.backend.service;

import com.furnihub.backend.dto.ProductDetailDto;
import com.furnihub.backend.dto.ProductDto;
import com.furnihub.backend.entity.Product;
import com.furnihub.backend.exception.ResourceNotFoundException;
import com.furnihub.backend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> getActiveProducts(Long categoryId, Pageable pageable) {
        Page<Product> page = categoryId != null
                ? productRepository.findByActiveTrueAndCategoryId(categoryId, pageable)
                : productRepository.findByActiveTrue(pageable);
        return page.map(ProductDto::from);
    }

    @Transactional(readOnly = true)
    public ProductDetailDto getActiveProduct(Long id) {
        Product product = productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return ProductDetailDto.from(product);
    }
}
