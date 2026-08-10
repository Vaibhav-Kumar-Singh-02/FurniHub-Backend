package com.furnihub.controller;

import com.furnihub.dto.InventoryResponse;
import com.furnihub.service.AdminInventoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inventory")
public class AdminInventoryController {

    private final AdminInventoryService adminInventoryService;

    public AdminInventoryController(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getInventory(@RequestParam(required = false) String search,
                                                            @RequestParam(required = false) String category,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<InventoryResponse> inventoryPage = adminInventoryService.getInventory(search, category, page, size);
        return ResponseEntity.ok(Map.of(
                "products", inventoryPage.getContent(),
                "totalPages", inventoryPage.getTotalPages()
        ));
    }

    @PostMapping("/{productId}/increase")
    public ResponseEntity<Void> increaseStock(@PathVariable Integer productId, @RequestParam Integer quantity) {
        adminInventoryService.increaseStock(productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/decrease")
    public ResponseEntity<Void> decreaseStock(@PathVariable Integer productId, @RequestParam Integer quantity) {
        adminInventoryService.decreaseStock(productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Integer productId, @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("quantity");
        if (quantity == null) {
            return ResponseEntity.badRequest().build();
        }
        if (quantity > 0) {
            adminInventoryService.increaseStock(productId, quantity);
        } else if (quantity < 0) {
            adminInventoryService.decreaseStock(productId, Math.abs(quantity));
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockProducts() {
        List<InventoryResponse> response = adminInventoryService.getLowStockProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<InventoryResponse>> getOutOfStockProducts() {
        List<InventoryResponse> response = adminInventoryService.getOutOfStockProducts();
        return ResponseEntity.ok(response);
    }
}
