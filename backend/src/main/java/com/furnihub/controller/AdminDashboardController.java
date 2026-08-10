package com.furnihub.controller;

import com.furnihub.dto.AnalyticsResponse;
import com.furnihub.service.AdminAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminAnalyticsService adminAnalyticsService;

    public AdminDashboardController(AdminAnalyticsService adminAnalyticsService) {
        this.adminAnalyticsService = adminAnalyticsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AnalyticsResponse> getStats() {
        return ResponseEntity.ok(adminAnalyticsService.getOverallAnalytics());
    }
}
