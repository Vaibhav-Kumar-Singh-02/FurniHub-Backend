package com.furnihub.service;

import com.furnihub.dto.AdminSettingsRequest;
import com.furnihub.dto.UserResponse;
import java.util.Map;

public interface AdminSettingsService {

    UserResponse getAdminProfile(Integer adminId);

    UserResponse updateAdminProfile(Integer adminId, AdminSettingsRequest request);

    UserResponse getAdminProfileByEmail(String email);

    UserResponse updateAdminProfileByEmail(String email, AdminSettingsRequest request);

    void changePassword(String email, String currentPassword, String newPassword);

    Map<String, String> getAppSettings();

    Map<String, String> updateAppSettings(Map<String, String> settings);
}