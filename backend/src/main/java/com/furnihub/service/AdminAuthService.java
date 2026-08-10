package com.furnihub.service;

import com.furnihub.config.JwtUtil;
import com.furnihub.dto.AuthResponse;
import com.furnihub.dto.LoginRequest;
import com.furnihub.entity.Admin;
import com.furnihub.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AdminAuthService.class);

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AdminAuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String emailOrMobile = request.getEmailOrMobile();

        Admin admin = adminRepository.findByEmail(emailOrMobile)
                .orElseGet(() -> adminRepository.findByMobile(emailOrMobile)
                        .orElse(null));

        if (admin == null) {
            return new AuthResponse("Invalid Email/Mobile Number or Password", false, null, null, null, null);
        }

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            return new AuthResponse("Invalid Email/Mobile Number or Password", false, null, null, null, null);
        }

        if (!admin.getIsActive()) {
            return new AuthResponse("Admin account is disabled", false, null, null, null, null);
        }

        String token = jwtUtil.generateToken(admin.getEmail());

        logger.info("Admin login successful for: {}", admin.getEmail());
        return new AuthResponse("Admin login successful", true, token, admin.getRole().name(), admin.getFullName(), admin.getAdminId());
    }
}
