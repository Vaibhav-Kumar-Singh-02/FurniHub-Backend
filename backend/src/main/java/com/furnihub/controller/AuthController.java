package com.furnihub.controller;

import com.furnihub.dto.AuthResponse;
import com.furnihub.dto.ChangePasswordRequest;
import com.furnihub.dto.ForgotPasswordRequest;
import com.furnihub.dto.LoginRequest;
import com.furnihub.dto.OtpVerifyRequest;
import com.furnihub.dto.RegisterRequest;
import com.furnihub.dto.ResetPasswordRequest;
import com.furnihub.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${app.cookie.secure:false}")
    private boolean cookieSecure;

    public AuthController(AuthService authService) {
        this.authService = authService;
    
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AuthResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        AuthResponse response = authService.forgotPassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AuthResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        AuthResponse response = authService.resetPassword(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<AuthResponse> changePassword(@RequestHeader("Authorization") String token,
                                                       @Valid @RequestBody ChangePasswordRequest request) {
        String jwtToken = token.replace("Bearer ", "");
        AuthResponse tokenValidation = authService.validateToken(jwtToken);
        if (!tokenValidation.isSuccess()) {
            return ResponseEntity.badRequest().body(tokenValidation);
        }
        AuthResponse response = authService.changePassword(tokenValidation.getUserId(), request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body(new AuthResponse("Unauthorized", false, null, null, null, null));
        }

        String email = authentication.getName();
        AuthResponse authResponse = authService.logoutByEmail(email);

        if (authResponse.isSuccess()) {
            clearAuthCookie(response);
            return ResponseEntity.ok(new AuthResponse("Logout successful", true, null, null, null, null));
        }

        return ResponseEntity.status(500).body(new AuthResponse("Logout failed", false, null, null, null, null));
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateToken(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        AuthResponse response = authService.validateToken(jwtToken);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    private void clearAuthCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("auth_token", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
