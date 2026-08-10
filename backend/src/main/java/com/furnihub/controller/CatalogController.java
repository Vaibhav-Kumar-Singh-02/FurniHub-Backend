package com.furnihub.controller;

import com.furnihub.dto.CategoryResponse;
import com.furnihub.dto.CouponResponse;
import com.furnihub.dto.CouponValidateResponse;
import com.furnihub.dto.ProductResponse;
import com.furnihub.entity.Coupon;
import com.furnihub.repository.CouponRepository;
import com.furnihub.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/categories")
    public List<CategoryResponse> getCategories() {
        return catalogService.getCategories();
    }

    @GetMapping("/products")
    public List<ProductResponse> getProducts() {
        return catalogService.getProducts();
    }

    @GetMapping("/products/search")
    public List<ProductResponse> searchProducts(@RequestParam String q) {
        return catalogService.searchProducts(q);
    }

    @GetMapping("/products/{id}")
    public ProductResponse getProductById(@PathVariable Integer id) {
        return catalogService.getProductById(id);
    }

    @GetMapping("/coupons/validate")
    public ResponseEntity<CouponValidateResponse> validateCoupon(
            @RequestParam String code,
            @RequestParam(required = false) BigDecimal subtotal,
            @RequestParam(required = false) List<Integer> productIds) {
        CouponValidateResponse response = catalogService.validateCoupon(code, subtotal, productIds);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/coupons/active")
    public ResponseEntity<List<CouponResponse>> getActiveCoupons() {
        List<CouponResponse> response = catalogService.getActiveCoupons();
        return ResponseEntity.ok(response);
    }
}
