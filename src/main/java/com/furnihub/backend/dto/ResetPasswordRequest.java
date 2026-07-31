package com.furnihub.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Email or mobile is required")
        String emailOrMobile,

        @NotBlank(message = "OTP is required")
        @Pattern(regexp = "^\\d{6}$", message = "OTP must be exactly 6 digits")
        String otp,

        @NotBlank(message = "New password is required")
        @Size(min = 8, max = 64, message = "Password must contain at least 8 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "Password must include at least one uppercase letter, one lowercase letter, one number and one special character")
        String newPassword
) {
}
