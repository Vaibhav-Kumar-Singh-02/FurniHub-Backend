package com.furnihub.controller;

import com.furnihub.dto.AnalyticsResponse;
import com.furnihub.dto.OrderResponse;
import com.furnihub.dto.UserResponse;
import com.furnihub.service.AdminAnalyticsService;
import com.furnihub.service.AdminOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {

    private final AdminAnalyticsService adminAnalyticsService;
    private final AdminOrderService adminOrderService;

    public AdminAnalyticsController(AdminAnalyticsService adminAnalyticsService, AdminOrderService adminOrderService) {
        this.adminAnalyticsService = adminAnalyticsService;
        this.adminOrderService = adminOrderService;
    }

    @GetMapping("/daily")
    public ResponseEntity<AnalyticsResponse> getDailyAnalytics() {
        return ResponseEntity.ok(adminAnalyticsService.getDailyAnalytics());
    }

    @GetMapping("/monthly")
    public ResponseEntity<AnalyticsResponse> getMonthlyAnalytics() {
        return ResponseEntity.ok(adminAnalyticsService.getMonthlyAnalytics());
    }

    @GetMapping("/yearly")
    public ResponseEntity<AnalyticsResponse> getYearlyAnalytics() {
        return ResponseEntity.ok(adminAnalyticsService.getYearlyAnalytics());
    }

    @GetMapping("/overall")
    public ResponseEntity<AnalyticsResponse> getOverallAnalytics() {
        return ResponseEntity.ok(adminAnalyticsService.getOverallAnalytics());
    }

    @GetMapping("/revenue")
    public ResponseEntity<Map<String, Object>> getRevenue(@RequestParam(defaultValue = "monthly") String period) {
        AnalyticsResponse analytics = getAnalyticsForPeriod(period);
        return ResponseEntity.ok(Map.of(
                "stats", analytics,
                "period", period
        ));
    }

    @GetMapping("/orders")
    public ResponseEntity<Map<String, Object>> getOrderStats(@RequestParam(defaultValue = "monthly") String period) {
        AnalyticsResponse analytics = getAnalyticsForPeriod(period);
        return ResponseEntity.ok(Map.of(
                "stats", analytics,
                "period", period
        ));
    }

    @GetMapping("/best-selling-products")
    public ResponseEntity<Map<String, Object>> getBestSellingProducts(@RequestParam(defaultValue = "monthly") String period) {
        AnalyticsResponse analytics = getAnalyticsForPeriod(period);
        return ResponseEntity.ok(Map.of(
                "products", analytics.getTopProducts(),
                "period", period
        ));
    }

    @GetMapping("/best-selling-categories")
    public ResponseEntity<Map<String, Object>> getBestSellingCategories(@RequestParam(defaultValue = "monthly") String period) {
        AnalyticsResponse analytics = getAnalyticsForPeriod(period);
        return ResponseEntity.ok(Map.of(
                "categories", analytics.getTopCategories(),
                "period", period
        ));
    }

    @GetMapping("/top-customers")
    public ResponseEntity<Map<String, Object>> getTopCustomers(@RequestParam(defaultValue = "monthly") String period) {
        AnalyticsResponse analytics = getAnalyticsForPeriod(period);
        return ResponseEntity.ok(Map.of(
                "customers", analytics.getTopCustomers(),
                "period", period
        ));
    }

    private AnalyticsResponse getAnalyticsForPeriod(String period) {
        return switch (period) {
            case "daily" -> adminAnalyticsService.getDailyAnalytics();
            case "yearly" -> adminAnalyticsService.getYearlyAnalytics();
            default -> adminAnalyticsService.getMonthlyAnalytics();
        };
    }
}
