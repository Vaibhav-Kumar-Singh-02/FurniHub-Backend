package com.furnihub.controller;

import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.OrderStatusUpdateRequest;
import com.furnihub.service.AdminOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders(@RequestParam(required = false) String status,
                                                            @RequestParam(required = false) String search,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orderPage = adminOrderService.getAllOrders(status, search, page, size);
        return ResponseEntity.ok(Map.of(
                "orders", orderPage.getContent(),
                "totalPages", orderPage.getTotalPages()
        ));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String orderId) {
        OrderResponse response = adminOrderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable String orderId,
                                                           @Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse response = adminOrderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        adminOrderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/refund")
    public ResponseEntity<OrderResponse> processRefund(@PathVariable String orderId) {
        OrderResponse response = adminOrderService.processRefund(orderId);
        return ResponseEntity.ok(response);
    }
}
