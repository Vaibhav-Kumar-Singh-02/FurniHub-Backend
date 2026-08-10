package com.furnihub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSettingsRequest {
    private String fullName;
    private String email;
    private String mobile;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}