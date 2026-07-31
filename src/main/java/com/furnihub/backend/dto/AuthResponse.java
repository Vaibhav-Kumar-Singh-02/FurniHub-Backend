package com.furnihub.backend.dto;

import com.furnihub.backend.entity.User;

public record AuthResponse(
        String message,
        boolean success,
        String token,
        String role,
        String fullName,
        Long userId
) {

    public static AuthResponse success(String message, String token, User user) {
        return new AuthResponse(
                message,
                true,
                token,
                user.getRole(),
                user.getFullName(),
                user.getId()
        );
    }

    public static AuthResponse successMessage(String message) {
        return new AuthResponse(message, true, null, null, null, null);
    }
}
