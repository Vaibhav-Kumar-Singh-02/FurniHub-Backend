package com.furnihub.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 255, message = "Full name must be at most 255 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 255, message = "Email must be at most 255 characters")
        String email,

        @NotBlank(message = "Mobile is required")
        @Pattern(regexp = "^\\d{10}$", message = "Mobile must contain exactly 10 digits")
        String mobile,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 64, message = "Password must contain at least 8 characters")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "Password must include at least one uppercase letter, one lowercase letter, one number and one special character")
        String password
) {
}
