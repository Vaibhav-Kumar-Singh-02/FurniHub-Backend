package com.furnihub.controller;

import com.furnihub.dto.ProductRequest;
import com.furnihub.dto.ProductResponse;
import com.furnihub.enums.ProductStatus;
import com.furnihub.service.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = adminProductService.addProduct(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Integer id, @Valid @RequestBody ProductRequest request) {
        ProductResponse response = adminProductService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(@RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String category,
                                                             @RequestParam(required = false) ProductStatus status,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        String statusStr = status != null ? status.name() : null;
        Page<ProductResponse> productPage = adminProductService.getAllProducts(search, category, statusStr, page, size);
        return ResponseEntity.ok(Map.of(
                "products", productPage.getContent(),
                "totalPages", productPage.getTotalPages()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Integer id) {
        ProductResponse response = adminProductService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        List<ProductResponse> response = adminProductService.searchProducts(keyword);
        return ResponseEntity.ok(response);
    }
}
