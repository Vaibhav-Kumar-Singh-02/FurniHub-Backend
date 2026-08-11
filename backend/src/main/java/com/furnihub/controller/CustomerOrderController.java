package com.furnihub.controller;

import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.ReceiptItemResponse;
import com.furnihub.dto.ReceiptResponse;
import com.furnihub.entity.Admin;
import com.furnihub.entity.Order;
import com.furnihub.entity.OrderItem;
import com.furnihub.entity.Product;
import com.furnihub.entity.ProductImage;
import com.furnihub.entity.User;
import com.furnihub.repository.AdminRepository;
import com.furnihub.repository.CouponRepository;
import com.furnihub.repository.OrderRepository;
import com.furnihub.repository.ProductImageRepository;
import com.furnihub.repository.ProductRepository;
import com.furnihub.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerOrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final ProductImageRepository productImageRepository;

    public CustomerOrderController(OrderRepository orderRepository,
                                   ProductRepository productRepository,
                                   UserRepository userRepository,
                                   CouponRepository couponRepository,
                                   ProductImageRepository productImageRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.productImageRepository = productImageRepository;
    }

    @PostMapping("/orders")
    public ResponseEntity<?> placeOrder(Authentication authentication,
                                        @RequestBody java.util.Map<String, Object> request) {
        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<java.util.Map<String, Object>> items = (List<java.util.Map<String, Object>>) request.get("items");
            String shippingAddress = (String) request.get("shippingAddress");
            String paymentMethod = (String) request.get("paymentMethod");
            String couponCode = (String) request.get("couponCode");

            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body("Cart is empty");
            }

            Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
            order.setUser(user);
            order.setShippingAddress(shippingAddress);
            order.setPaymentMethod(paymentMethod);
            order.setStatus(Order.OrderStatus.CONFIRMED);
            order.setCreatedAt(LocalDateTime.now());
            order.setUpdatedAt(LocalDateTime.now());

            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (java.util.Map<String, Object> item : items) {
                Integer productId = (Integer) item.get("productId");
                Integer quantity = (Integer) item.get("quantity");

                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

                if (product.getStock() < quantity) {
                    return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
                }

                product.setStock(product.getStock() - quantity);
                productRepository.save(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(quantity);
                orderItem.setPricePerUnit(product.getPrice());
                orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

                orderItems.add(orderItem);
                totalAmount = totalAmount.add(orderItem.getTotalPrice());
            }

            BigDecimal discountAmount = BigDecimal.ZERO;
            if (couponCode != null && !couponCode.isBlank()) {
                com.furnihub.entity.Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(couponCode.toUpperCase())
                        .orElse(null);
                if (coupon != null && coupon.getValidUntil() != null && LocalDateTime.now().isBefore(coupon.getValidUntil())) {
                    if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType().name())) {
                        discountAmount = totalAmount.multiply(coupon.getDiscountValue().divide(new BigDecimal("100")));
                    } else {
                        discountAmount = coupon.getDiscountValue();
                    }
                    if (coupon.getMaxDiscountAmount() != null && discountAmount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                        discountAmount = coupon.getMaxDiscountAmount();
                    }
                    if (totalAmount.subtract(discountAmount).compareTo(BigDecimal.ZERO) < 0) {
                        discountAmount = totalAmount;
                    }
                    coupon.setUsedCount(coupon.getUsedCount() + 1);
                    couponRepository.save(coupon);
                }
            }

            BigDecimal gstRate = new BigDecimal("0.18");
            BigDecimal cgst = totalAmount.multiply(gstRate).divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal sgst = totalAmount.multiply(gstRate).divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP);
            BigDecimal gstAmount = cgst.add(sgst);
            BigDecimal grandTotal = totalAmount.subtract(discountAmount).add(gstAmount);

            order.setOrderItems(orderItems);
            order.setTotalAmount(grandTotal);
            orderRepository.save(order);

            OrderResponse response = mapToResponse(order);
            response.setDeliveryDate(order.getCreatedAt().plusDays(5).toString());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to place order: " + e.getMessage());
        }
    }

    @GetMapping("/orders")
    @Transactional(readOnly = true)
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUser_UserIdWithUser(user.getUserId());
        List<OrderResponse> response = orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{orderId}")
    @Transactional(readOnly = true)
    public ResponseEntity<OrderResponse> getOrderById(Authentication authentication,
                                                       @PathVariable String orderId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        Order orderWithUser = orderRepository.findByUser_UserIdWithUser(user.getUserId()).stream()
                .filter(o -> o.getOrderId().equals(orderId))
                .findFirst()
                .orElse(order);

        return ResponseEntity.ok(mapToResponse(orderWithUser));
    }

    @GetMapping("/orders/{orderId}/receipt")
    @Transactional(readOnly = true)
    public ResponseEntity<ReceiptResponse> getReceipt(Authentication authentication,
                                                       @PathVariable String orderId) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        ReceiptResponse receipt = new ReceiptResponse();
        receipt.setOrderId(order.getOrderId());
        receipt.setCustomerName(order.getUser().getFullName());
        receipt.setCustomerEmail(order.getUser().getEmail());
        receipt.setCustomerMobile(order.getUser().getMobile());
        receipt.setShippingAddress(order.getShippingAddress());
        receipt.setPaymentMethod(order.getPaymentMethod());
        receipt.setStatus(order.getStatus().name());
        receipt.setOrderDate(order.getCreatedAt());
        receipt.setDeliveryDate(order.getCreatedAt() != null ? order.getCreatedAt().plusDays(5).toString() : null);

        List<ReceiptItemResponse> items = order.getOrderItems().stream()
                .map(item -> {
                    List<ProductImage> images = productImageRepository.findByProductId(item.getProduct().getProductId());
                    String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
                    return new ReceiptItemResponse(
                            item.getProduct().getName(),
                            item.getProduct().getBrand(),
                            item.getQuantity(),
                            item.getPricePerUnit(),
                            item.getTotalPrice(),
                            imageUrl
                    );
                })
                .collect(Collectors.toList());
        receipt.setItems(items);

        BigDecimal subtotal = items.stream()
                .map(ReceiptItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        receipt.setSubtotal(subtotal);

        BigDecimal cgst = subtotal.multiply(new BigDecimal("0.09")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal sgst = subtotal.multiply(new BigDecimal("0.09")).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal gstTotal = cgst.add(sgst);
        BigDecimal grandTotal = subtotal.add(gstTotal);

        receipt.setCgst(cgst);
        receipt.setSgst(sgst);
        receipt.setIgst(BigDecimal.ZERO);
        receipt.setGstTotal(gstTotal);
        receipt.setTotalAmount(grandTotal);

        return ResponseEntity.ok(receipt);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setUserFullName(order.getUser().getFullName());
        response.setUserEmail(order.getUser().getEmail());
        response.setUserMobile(order.getUser().getMobile());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus().name());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setShippingAddress(order.getShippingAddress());
        response.setDeliveryDate(order.getCreatedAt() != null ? order.getCreatedAt().plusDays(5).toString() : null);
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());

        List<com.furnihub.dto.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    com.furnihub.dto.OrderItemResponse itemResponse = new com.furnihub.dto.OrderItemResponse();
                    itemResponse.setOrderItemId(item.getOrderItemId());
                    itemResponse.setOrderId(order.getOrderId());
                    itemResponse.setProductId(item.getProduct().getProductId());
                    itemResponse.setProductName(item.getProduct().getName());
                    itemResponse.setBrand(item.getProduct().getBrand());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPricePerUnit(item.getPricePerUnit());
                    itemResponse.setTotalPrice(item.getTotalPrice());
                    List<ProductImage> images = productImageRepository.findByProductId(item.getProduct().getProductId());
                    if (!images.isEmpty()) {
                        itemResponse.setImageUrl(images.get(0).getImageUrl());
                    }
                    return itemResponse;
                })
                .collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
