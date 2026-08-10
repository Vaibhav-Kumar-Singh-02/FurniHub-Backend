package com.furnihub.service;

import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.OrderStatusUpdateRequest;
import com.furnihub.entity.Order;
import com.furnihub.entity.OrderItem;
import com.furnihub.entity.Product;
import com.furnihub.repository.OrderItemRepository;
import com.furnihub.repository.OrderRepository;
import com.furnihub.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AdminOrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public AdminOrderServiceImpl(OrderRepository orderRepository,
                                     OrderItemRepository orderItemRepository,
                                     ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(String status, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Order> orders;

        if (search != null && !search.isBlank()) {
            orders = orderRepository.findAllWithUser();
        } else if (status != null && !status.isBlank()) {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status);
            orders = orderRepository.findByStatusWithUser(orderStatus);
        } else {
            orders = orderRepository.findAllWithUser();
        }

        List<OrderResponse> responses = orders.stream()
                .skip((long) page * size)
                .limit(size)
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return new org.springframework.data.domain.PageImpl<>(responses, pageable, orders.size());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        Order orderWithUser = orderRepository.findAllWithUser().stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(order);
        
        return mapToResponse(orderWithUser);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String orderId, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        Order.OrderStatus newStatus = Order.OrderStatus.valueOf(request.getStatus().toUpperCase());
        order.setStatus(newStatus);

        order = orderRepository.save(order);

        logger.info("Order status updated for order {}: {}", orderId, newStatus);
        return mapToResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);

        logger.info("Order cancelled with id: {}", orderId);
    }

    @Override
    @Transactional
    public OrderResponse processRefund(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(Order.OrderStatus.REFUNDED);
        order = orderRepository.save(order);

        logger.info("Refund processed for order id: {}", orderId);
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        List<com.furnihub.dto.OrderItemResponse> itemResponses = new ArrayList<>();
        for (OrderItem item : orderItems) {
            Product product = productRepository.findById(item.getProduct().getProductId()).orElse(null);
            String productName = product != null ? product.getName() : null;

            com.furnihub.dto.OrderItemResponse itemResponse = new com.furnihub.dto.OrderItemResponse();
            itemResponse.setOrderItemId(item.getOrderItemId());
            itemResponse.setOrderId(item.getOrder().getOrderId());
            itemResponse.setProductId(item.getProduct().getProductId());
            itemResponse.setProductName(productName);
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPricePerUnit(item.getPricePerUnit());
            itemResponse.setTotalPrice(item.getTotalPrice());
            itemResponses.add(itemResponse);
        }

        Integer userId = order.getUser() != null ? order.getUser().getUserId() : null;
        String fullName = order.getUser() != null ? order.getUser().getFullName() : null;
        String email = order.getUser() != null ? order.getUser().getEmail() : null;

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(userId);
        response.setUserFullName(fullName);
        response.setUserEmail(email);
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setShippingAddress(order.getShippingAddress());
        response.setDeliveryDate(order.getCreatedAt() != null ? order.getCreatedAt().plusDays(5).toString() : null);
        response.setItems(itemResponses);
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        return response;
    }
}