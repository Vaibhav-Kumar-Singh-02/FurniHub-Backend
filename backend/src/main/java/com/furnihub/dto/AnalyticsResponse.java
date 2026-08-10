package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalCustomers;
    private Long totalProducts;
    private Long lowStockProducts;
    private List<OrderResponse> recentOrders;
    private List<UserResponse> recentCustomers;
    private List<Map<String, Object>> revenueByDay;
    private Map<String, Long> ordersByStatus;
    private List<Map<String, Object>> topProducts;
    private List<Map<String, Object>> topCategories;
    private List<Map<String, Object>> topCustomers;
    private BigDecimal revenue;
    private BigDecimal averageOrderValue;
    private Map<String, Long> orderStats;
}