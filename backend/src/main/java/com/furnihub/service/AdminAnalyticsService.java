package com.furnihub.service;

import com.furnihub.dto.AnalyticsResponse;

public interface AdminAnalyticsService {

    AnalyticsResponse getDailyAnalytics();

    AnalyticsResponse getMonthlyAnalytics();

    AnalyticsResponse getYearlyAnalytics();

    AnalyticsResponse getOverallAnalytics();
}