package com.furnihub.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @NotBlank(message = "Email or mobile is required")
        String emailOrMobile
) {
}
