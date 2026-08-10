package com.furnihub.controller;

import com.furnihub.dto.AdminSettingsRequest;
import com.furnihub.dto.UserResponse;
import com.furnihub.service.AdminSettingsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/settings")
public class AdminSettingsController {

    private final AdminSettingsService adminSettingsService;

    public AdminSettingsController(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getAdminProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(adminSettingsService.getAdminProfileByEmail(email));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateAdminProfile(Authentication authentication, @Valid @RequestBody AdminSettingsRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(adminSettingsService.updateAdminProfileByEmail(email, request));
    }

    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(Authentication authentication, @RequestBody Map<String, String> request) {
        String email = authentication.getName();
        String currentPassword = request.get("currentPassword");
        String newPassword = request.get("newPassword");
        String confirmPassword = request.get("confirmPassword");

        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body(Map.of("message", "New passwords do not match"));
        }

        try {
            adminSettingsService.changePassword(email, currentPassword, newPassword);
            return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
        }
    }

    @GetMapping("/app")
    public ResponseEntity<Map<String, String>> getAppSettings() {
        return ResponseEntity.ok(Map.of(
                "siteName", "FurniHub",
                "siteDescription", "Comfortable Living Spaces",
                "supportEmail", "support@furnihub.com",
                "currency", "INR"
        ));
    }

    @PutMapping("/app")
    public ResponseEntity<Map<String, String>> updateAppSettings(@RequestBody Map<String, String> settings) {
        return ResponseEntity.ok(Map.of("message", "Application settings updated successfully"));
    }
}
