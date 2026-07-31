package com.furnihub.backend.service;

import com.furnihub.backend.dto.CreateOrderRequest;
import com.furnihub.backend.dto.OrderDto;
import com.furnihub.backend.entity.CartItem;
import com.furnihub.backend.entity.Order;
import com.furnihub.backend.entity.OrderItem;
import com.furnihub.backend.entity.Product;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.exception.CartEmptyException;
import com.furnihub.backend.exception.ResourceNotFoundException;
import com.furnihub.backend.repository.CartItemRepository;
import com.furnihub.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderService {

    private static final DateTimeFormatter ORDER_NUMBER_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;

    public OrderService(OrderRepository orderRepository, CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public OrderDto createOrder(User user, CreateOrderRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByUserIdOrderByCreatedAtAsc(user.getId());
        if (cartItems.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setStatus("PENDING");
        order.setShippingAddress(request.shippingAddress());
        order.setShippingCity(request.shippingCity());
        order.setShippingState(request.shippingState());
        order.setShippingZip(request.shippingZip());
        order.setShippingCountry(request.shippingCountry());
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            BigDecimal unitPrice = product.getDiscountPrice() != null
                    ? product.getDiscountPrice()
                    : product.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(lineTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(unitPrice);
            orderItem.setTotalPrice(lineTotal);
            order.getOrderItems().add(orderItem);

            product.setStockQuantity(Math.max(0, product.getStockQuantity() - cartItem.getQuantity()));
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return OrderDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId).stream()
                .map(OrderDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDto getUserOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return OrderDto.from(order);
    }

    private String generateOrderNumber() {
        String base = "FH-" + LocalDateTime.now().format(ORDER_NUMBER_FORMAT);
        String candidate;
        do {
            candidate = base + ThreadLocalRandom.current().nextInt(1000, 10000);
        } while (orderRepository.existsByOrderNumber(candidate));
        return candidate;
    }
}
