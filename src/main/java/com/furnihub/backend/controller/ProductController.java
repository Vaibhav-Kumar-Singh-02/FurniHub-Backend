package com.furnihub.backend.controller;

import com.furnihub.backend.dto.ProductDetailDto;
import com.furnihub.backend.dto.ProductDto;
import com.furnihub.backend.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductDto> getProducts(@RequestParam(required = false) Long categoryId,
                                        @PageableDefault(size = 12) Pageable pageable) {
        return productService.getActiveProducts(categoryId, pageable);
    }

    @GetMapping("/{id}")
    public ProductDetailDto getProduct(@PathVariable Long id) {
        return productService.getActiveProduct(id);
    }
}
