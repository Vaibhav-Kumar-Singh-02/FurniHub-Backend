package com.furnihub.service;

import com.furnihub.dto.AnalyticsResponse;
import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.UserResponse;
import com.furnihub.entity.Order;
import com.furnihub.entity.Product;
import com.furnihub.entity.Review;
import com.furnihub.entity.User;
import com.furnihub.repository.OrderRepository;
import com.furnihub.repository.ProductRepository;
import com.furnihub.repository.ReviewRepository;
import com.furnihub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAnalyticsServiceImpl.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public AdminAnalyticsServiceImpl(OrderRepository orderRepository,
                                        ProductRepository productRepository,
                                        UserRepository userRepository,
                                        ReviewRepository reviewRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponse getDailyAnalytics() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        return buildAnalytics(startOfDay, endOfDay);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponse getMonthlyAnalytics() {
        YearMonth yearMonth = YearMonth.now();
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        return buildAnalytics(startOfMonth, endOfMonth);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponse getYearlyAnalytics() {
        int currentYear = LocalDate.now().getYear();
        LocalDateTime startOfYear = LocalDateTime.of(currentYear, 1, 1, 0, 0, 0);
        LocalDateTime endOfYear = LocalDateTime.of(currentYear, 12, 31, 23, 59, 59);

        return buildAnalytics(startOfYear, endOfYear);
    }

    @Override
    @Transactional(readOnly = true)
    public AnalyticsResponse getOverallAnalytics() {
        return buildAnalytics(null, null);
    }

    private AnalyticsResponse buildAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> allOrders = orderRepository.findAll();
        List<Order> orders;
        if (startDate != null && endDate != null) {
            orders = allOrders.stream()
                    .filter(o -> o.getCreatedAt() != null && !o.getCreatedAt().isBefore(startDate) && !o.getCreatedAt().isAfter(endDate))
                    .collect(Collectors.toList());
        } else {
            orders = allOrders;
        }

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalOrders = orders.stream()
                .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                .count();

        long totalCustomers = orders.stream()
                .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                .map(order -> order.getUser() != null ? order.getUser().getUserId() : null)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .count();

        long totalProducts = productRepository.count();

        long lowStockProducts = productRepository.findAll().stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0 && p.getStock() <= 10)
                .count();

        List<OrderResponse> recentOrders = orders.stream()
                .filter(o -> o.getCreatedAt() != null)
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .limit(10)
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());

        List<UserResponse> recentCustomers = userRepository.findAll().stream()
                .filter(u -> u.getCreatedAt() != null)
                .sorted((u1, u2) -> u2.getCreatedAt().compareTo(u1.getCreatedAt()))
                .limit(10)
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());

        java.util.List<java.util.Map<String, Object>> revenueByDayList = orders.stream()
                .filter(o -> o.getCreatedAt() != null && o.getStatus() != Order.OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate().toString(),
                        Collectors.reducing(BigDecimal.ZERO, Order::getTotalAmount, BigDecimal::add)
                ))
                .entrySet().stream()
                .map(entry -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("date", entry.getKey());
                    map.put("revenue", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        java.util.Map<String, Long> ordersByStatus = orders.stream()
                .filter(o -> o.getStatus() != null)
                .collect(Collectors.groupingBy(
                        o -> o.getStatus().name(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> topProducts = productRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getProductId()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(
                        e2.getValue().stream().mapToLong(p -> p.getStock()).sum(),
                        e1.getValue().stream().mapToLong(p -> p.getStock()).sum()
                ))
                .limit(10)
                .map(e -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("productId", e.getKey());
                    map.put("name", e.getValue().get(0).getName());
                    map.put("totalStock", e.getValue().stream().mapToLong(Product::getStock).sum());
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> topCategories = productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getCategorieId))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(
                        (long) e2.getValue().stream().mapToDouble(p -> p.getPrice().doubleValue() * p.getStock()).sum(),
                        (long) e1.getValue().stream().mapToDouble(p -> p.getPrice().doubleValue() * p.getStock()).sum()
                ))
                .limit(10)
                .map(e -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("categoryId", e.getKey());
                    map.put("totalRevenue", e.getValue().stream()
                            .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getStock())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> topCustomers = orders.stream()
                .filter(o -> o.getStatus() != Order.OrderStatus.CANCELLED)
                .collect(Collectors.groupingBy(o -> o.getUser().getUserId()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().stream()
                        .map(Order::getTotalAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(e1.getValue().stream()
                                .map(Order::getTotalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)))
                .limit(10)
                .map(e -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("userId", e.getKey());
                    map.put("totalSpent", e.getValue().stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
                    return map;
                })
                .collect(Collectors.toList());

        BigDecimal averageOrderValue = totalOrders > 0 ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;

        List<Map<String, Object>> enrichedTopProducts = topProducts.stream()
                .map(p -> {
                    Map<String, Object> map = new java.util.HashMap<>(p);
                    map.put("name", p.getOrDefault("name", "Product " + p.get("productId")));
                    map.put("unitsSold", p.getOrDefault("totalStock", 0));
                    map.put("sales", p.getOrDefault("totalStock", 0));
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> enrichedTopCategories = topCategories.stream()
                .map(c -> {
                    Map<String, Object> map = new java.util.HashMap<>(c);
                    map.put("name", c.getOrDefault("name", "Category " + c.get("categoryId")));
                    map.put("sales", c.getOrDefault("totalRevenue", BigDecimal.ZERO));
                    map.put("unitsSold", c.getOrDefault("totalRevenue", BigDecimal.ZERO));
                    return map;
                })
                .collect(Collectors.toList());

        List<Map<String, Object>> enrichedTopCustomers = topCustomers.stream()
                .map(c -> {
                    Map<String, Object> map = new java.util.HashMap<>(c);
                    map.put("fullName", c.getOrDefault("fullName", "Customer " + c.get("userId")));
                    map.put("totalSpent", c.getOrDefault("totalSpent", BigDecimal.ZERO));
                    map.put("orderValue", c.getOrDefault("totalSpent", BigDecimal.ZERO));
                    return map;
                })
                .collect(Collectors.toList());

        AnalyticsResponse response = new AnalyticsResponse();
        response.setTotalRevenue(totalRevenue);
        response.setTotalOrders(totalOrders);
        response.setTotalCustomers(totalCustomers);
        response.setTotalProducts(totalProducts);
        response.setLowStockProducts(lowStockProducts);
        response.setRecentOrders(recentOrders);
        response.setRecentCustomers(recentCustomers);
        response.setRevenueByDay(revenueByDayList);
        response.setOrdersByStatus(ordersByStatus);
        response.setTopProducts(enrichedTopProducts);
        response.setTopCategories(enrichedTopCategories);
        response.setTopCustomers(enrichedTopCustomers);
        response.setRevenue(totalRevenue);
        response.setAverageOrderValue(averageOrderValue);
        response.setOrderStats(ordersByStatus);

        return response;
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<com.furnihub.dto.OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    com.furnihub.dto.OrderItemResponse ir = new com.furnihub.dto.OrderItemResponse();
                    ir.setOrderItemId(item.getOrderItemId());
                    ir.setOrderId(item.getOrder().getOrderId());
                    ir.setProductId(item.getProduct().getProductId());
                    ir.setProductName(item.getProduct().getName());
                    ir.setQuantity(item.getQuantity());
                    ir.setPricePerUnit(item.getPricePerUnit());
                    ir.setTotalPrice(item.getTotalPrice());
                    return ir;
                })
                .collect(Collectors.toList());

        com.furnihub.dto.OrderResponse response = new com.furnihub.dto.OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setUserFullName(order.getUser().getFullName());
        response.setUserEmail(order.getUser().getEmail());
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

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getRole().name(),
                null,
                user.getCreatedAt()
        );
    }
}