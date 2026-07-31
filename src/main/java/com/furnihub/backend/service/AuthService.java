package com.furnihub.backend.service;

import com.furnihub.backend.dto.AuthResponse;
import com.furnihub.backend.dto.ChangePasswordRequest;
import com.furnihub.backend.dto.ForgotPasswordRequest;
import com.furnihub.backend.dto.LoginRequest;
import com.furnihub.backend.dto.RegisterRequest;
import com.furnihub.backend.dto.ResetPasswordRequest;
import com.furnihub.backend.dto.VerifyOtpRequest;
import com.furnihub.backend.entity.JwtToken;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.exception.AccountNotFoundException;
import com.furnihub.backend.exception.DuplicateEmailException;
import com.furnihub.backend.exception.DuplicateMobileException;
import com.furnihub.backend.exception.InvalidCredentialsException;
import com.furnihub.backend.exception.InvalidOtpException;
import com.furnihub.backend.exception.OtpExpiredException;
import com.furnihub.backend.repository.JwtTokenRepository;
import com.furnihub.backend.repository.UserRepository;
import com.furnihub.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AuthService {

    private static final long OTP_VALIDITY_MINUTES = 10;

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SecureRandom secureRandom = new SecureRandom();

    public AuthService(UserRepository userRepository,
                       JwtTokenRepository jwtTokenRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        String mobile = request.mobile().trim();

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Email is already registered");
        }
        if (userRepository.existsByMobile(mobile)) {
            throw new DuplicateMobileException("Mobile number is already registered");
        }

        User user = new User();
        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setMobile(mobile);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole("CUSTOMER");

        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);
        saveToken(token, saved);

        return AuthResponse.success("Registration successful", token, saved);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String identifier = request.emailOrMobile().trim();
        User user = findUser(identifier);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email/mobile or password");
        }

        String token = jwtService.generateToken(user);
        saveToken(token, user);

        return AuthResponse.success("Login successful", token, user);
    }

    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        String identifier = request.emailOrMobile().trim();
        User user = userRepository.findByEmailOrMobile(
                identifier.toLowerCase(Locale.ROOT),
                identifier
        ).orElseThrow(() -> new AccountNotFoundException("No account found with that email or mobile"));

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES));
        userRepository.save(user);

        return new AuthResponse(
                "Password reset OTP sent. Demo OTP: " + otp,
                true,
                null,
                null,
                null,
                null
        );
    }

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        String identifier = request.emailOrMobile().trim();
        User user = userRepository.findByEmailOrMobile(
                identifier.toLowerCase(Locale.ROOT),
                identifier
        ).orElseThrow(() -> new AccountNotFoundException("No account found with that email or mobile"));

        validateOtp(user, request.otp());

        return AuthResponse.successMessage("OTP verified successfully");
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        String identifier = request.emailOrMobile().trim();
        User user = userRepository.findByEmailOrMobile(
                identifier.toLowerCase(Locale.ROOT),
                identifier
        ).orElseThrow(() -> new AccountNotFoundException("No account found with that email or mobile"));

        validateOtp(user, request.otp());

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setOtp(null);
        user.setOtpExpiry(null);
        revokeAllTokens(user);
        userRepository.save(user);

        return AuthResponse.successMessage("Password reset successful. Please login with your new password");
    }

    @Transactional
    public AuthResponse changePassword(User user, ChangePasswordRequest request) {
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        revokeAllTokens(user);
        userRepository.save(user);

        return AuthResponse.successMessage("Password changed successfully");
    }

    @Transactional
    public AuthResponse logout(String token) {
        jwtTokenRepository.findByToken(token).ifPresent(jwtToken -> {
            jwtToken.setRevoked(true);
            jwtTokenRepository.save(jwtToken);
        });
        return AuthResponse.successMessage("Logged out successfully");
    }

    @Transactional(readOnly = true)
    public AuthResponse validate(User user) {
        return AuthResponse.success("Token is valid", null, user);
    }

    private User findUser(String identifier) {
        return userRepository.findByEmailOrMobile(
                identifier.toLowerCase(Locale.ROOT),
                identifier
        ).orElseThrow(() -> new InvalidCredentialsException("Invalid email/mobile or password"));
    }

    private void validateOtp(User user, String otp) {
        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new InvalidOtpException("No OTP was requested for this account");
        }
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired. Please request a new one");
        }
        if (!user.getOtp().equals(otp)) {
            throw new InvalidOtpException("Invalid OTP");
        }
    }

    private void revokeAllTokens(User user) {
        jwtTokenRepository.findByUserIdAndRevokedFalse(user.getId())
                .forEach(token -> {
                    token.setRevoked(true);
                    jwtTokenRepository.save(token);
                });
    }

    private void saveToken(String token, User user) {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setTokenType("BEARER");
        jwtToken.setRevoked(false);
        jwtToken.setUser(user);
        jwtTokenRepository.save(jwtToken);
    }

    private String generateOtp() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }
}
