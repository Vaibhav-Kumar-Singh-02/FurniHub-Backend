package com.furnihub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerifyRequest {
    @NotBlank(message = "Email or Mobile is required")
    private String emailOrMobile;

    @NotBlank(message = "OTP is required")
    private String otp;
}
