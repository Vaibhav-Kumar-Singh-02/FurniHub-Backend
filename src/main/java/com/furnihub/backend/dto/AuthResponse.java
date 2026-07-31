package com.furnihub.backend.dto;

public record AuthResponse(String token, String tokenType, UserDto user) {
}
