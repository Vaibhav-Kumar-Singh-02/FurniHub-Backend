package com.furnihub.service;

import com.furnihub.config.JwtUtil;
import com.furnihub.dto.*;
import com.furnihub.entity.JwtToken;
import com.furnihub.entity.OtpVerification;
import com.furnihub.entity.User;
import com.furnihub.repository.JwtTokenRepository;
import com.furnihub.repository.OtpVerificationRepository;
import com.furnihub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       JwtTokenRepository jwtTokenRepository,
                       OtpVerificationRepository otpVerificationRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.otpVerificationRepository = otpVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("Passwords do not match", false, null, null, null, null);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse("Email already exists", false, null, null, null, null);
        }

        if (userRepository.existsByMobile(request.getMobile())) {
            return new AuthResponse("Mobile number already exists", false, null, null, null, null);
        }

        User user = new User();
        user.setUsername(request.getEmail());
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.CUSTOMER);

        user = userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        saveJwtToken(user, token);

        return new AuthResponse("Registration successful", true, token, user.getRole().name(), user.getFullName(), user.getUserId());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String emailOrMobile = request.getEmailOrMobile();

        User user = userRepository.findByEmail(emailOrMobile)
                .orElseGet(() -> userRepository.findByMobile(emailOrMobile)
                        .orElse(null));

        if (user == null) {
            return new AuthResponse("Invalid Email/Mobile Number or Password", false, null, null, null, null);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse("Invalid Email/Mobile Number or Password", false, null, null, null, null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        saveJwtToken(user, token);

        return new AuthResponse("Login successful", true, token, user.getRole().name(), user.getFullName(), user.getUserId());
    }

    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        String emailOrMobile = request.getEmailOrMobile();

        User user = userRepository.findByEmail(emailOrMobile)
                .orElseGet(() -> userRepository.findByMobile(emailOrMobile)
                        .orElse(null));

        if (user == null) {
            return new AuthResponse("No account found with this email or mobile number", false, null, null, null, null);
        }

        otpVerificationRepository.deleteByUser(user);

        String otp = String.format("%06d", new Random().nextInt(999999));

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setUser(user);
        otpVerification.setOtp(otp);
        otpVerification.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpVerificationRepository.save(otpVerification);

        // In production, send via email/SMS. For now, return OTP in response for simulation.
        return new AuthResponse("OTP sent successfully. Your OTP is: " + otp, true, null, null, null, null);
    }

    @Transactional
    public AuthResponse verifyOtp(OtpVerifyRequest request) {
        String emailOrMobile = request.getEmailOrMobile();

        User user = userRepository.findByEmail(emailOrMobile)
                .orElseGet(() -> userRepository.findByMobile(emailOrMobile)
                        .orElse(null));

        if (user == null) {
            return new AuthResponse("No account found", false, null, null, null, null);
        }

        OtpVerification otpVerification = otpVerificationRepository
                .findByUserAndOtpAndVerifiedFalse(user, request.getOtp())
                .orElse(null);

        if (otpVerification == null) {
            return new AuthResponse("Invalid OTP", false, null, null, null, null);
        }

        if (otpVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new AuthResponse("OTP has expired", false, null, null, null, null);
        }

        otpVerification.setVerified(true);
        otpVerificationRepository.save(otpVerification);

        return new AuthResponse("OTP verified successfully", true, null, null, null, null);
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("Passwords do not match", false, null, null, null, null);
        }

        String emailOrMobile = request.getEmailOrMobile();

        User user = userRepository.findByEmail(emailOrMobile)
                .orElseGet(() -> userRepository.findByMobile(emailOrMobile)
                        .orElse(null));

        if (user == null) {
            return new AuthResponse("No account found", false, null, null, null, null);
        }

        OtpVerification otpVerification = otpVerificationRepository
                .findByUserAndOtpAndVerifiedTrue(user, request.getOtp())
                .orElse(null);

        if (otpVerification == null) {
            return new AuthResponse("Please verify OTP first", false, null, null, null, null);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otpVerificationRepository.deleteByUser(user);

        return new AuthResponse("Password reset successful", true, null, null, null, null);
    }

    @Transactional
    public AuthResponse changePassword(Integer userId, ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("Passwords do not match", false, null, null, null, null);
        }

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return new AuthResponse("User not found", false, null, null, null, null);
        }

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return new AuthResponse("Current password is incorrect", false, null, null, null, null);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return new AuthResponse("Password changed successfully", true, null, null, null, null);
    }

    @Transactional
    public AuthResponse logout(String token) {
        JwtToken jwtToken = jwtTokenRepository.findByToken(token).orElse(null);
        if (jwtToken != null) {
            jwtTokenRepository.delete(jwtToken);
        }
        return new AuthResponse("Logout successful", true, null, null, null, null);
    }

    @Transactional
    public AuthResponse logoutByEmail(String email) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                jwtTokenRepository.deleteByUser(user);
                logger.info("Invalidated all JWT tokens for user: {}", email);
            }
            return new AuthResponse("Logout successful", true, null, null, null, null);
        } catch (Exception ex) {
            logger.error("Logout failed for user: {}", email, ex);
            return new AuthResponse("Logout failed", false, null, null, null, null);
        }
    }

    public AuthResponse validateToken(String token) {
        if (jwtUtil.validateToken(token)) {
            String email = jwtUtil.getEmailFromToken(token);
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                return new AuthResponse("Token is valid", true, token, user.getRole().name(), user.getFullName(), user.getUserId());
            }
        }
        return new AuthResponse("Invalid or expired token", false, null, null, null, null);
    }

    private void saveJwtToken(User user, String token) {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setUser(user);
        jwtToken.setToken(token);
        jwtToken.setExpiresAt(LocalDateTime.now().plusDays(30));
        jwtTokenRepository.save(jwtToken);
    }
}
