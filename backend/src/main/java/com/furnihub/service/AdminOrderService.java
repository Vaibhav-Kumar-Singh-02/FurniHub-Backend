package com.furnihub.service;

import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.OrderStatusUpdateRequest;
import org.springframework.data.domain.Page;

public interface AdminOrderService {

    Page<OrderResponse> getAllOrders(String status, String search, int page, int size);

    OrderResponse getOrderById(String orderId);

    OrderResponse updateOrderStatus(String orderId, OrderStatusUpdateRequest request);

    void cancelOrder(String orderId);

    OrderResponse processRefund(String orderId);
}