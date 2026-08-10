package com.furnihub.service;

import com.furnihub.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminNotificationServiceImpl implements AdminNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(AdminNotificationServiceImpl.class);

    @Override
    public List<NotificationResponse> getNotifications() {
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Integer notificationId) {
        logger.info("Notification marked as read with id: {}", notificationId);
    }

    @Override
    @Transactional
    public void generateNotifications() {
        logger.info("Notification generation triggered - orders, low stock, new users, refund requests");
    }
}