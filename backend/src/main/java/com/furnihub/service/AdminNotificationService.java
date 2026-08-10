package com.furnihub.service;

import com.furnihub.dto.NotificationResponse;

import java.util.List;

public interface AdminNotificationService {

    List<NotificationResponse> getNotifications();

    void markNotificationAsRead(Integer notificationId);

    void generateNotifications();
}