package com.furnihub.controller;

import com.furnihub.dto.NotificationResponse;
import com.furnihub.service.AdminNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/notifications")
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    public AdminNotificationController(AdminNotificationService adminNotificationService) {
        this.adminNotificationService = adminNotificationService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotifications(@RequestParam(required = false) String status,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        List<NotificationResponse> notifications = adminNotificationService.getNotifications();
        long unreadCount = notifications.stream().filter(n -> !n.getIsRead()).count();
        return ResponseEntity.ok(Map.of(
                "notifications", notifications,
                "unreadCount", unreadCount
        ));
    }

    @GetMapping("/unread")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications() {
        List<NotificationResponse> notifications = adminNotificationService.getNotifications().stream()
                .filter(n -> !n.getIsRead())
                .toList();
        return ResponseEntity.ok(Map.of(
                "notifications", notifications,
                "unreadCount", (long) notifications.size()
        ));
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Integer notificationId) {
        adminNotificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer notificationId) {
        adminNotificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateNotification(@RequestBody(required = false) Map<String, String> request) {
        adminNotificationService.generateNotifications();
        return ResponseEntity.ok(Map.of("message", "Notifications generated successfully"));
    }
}
