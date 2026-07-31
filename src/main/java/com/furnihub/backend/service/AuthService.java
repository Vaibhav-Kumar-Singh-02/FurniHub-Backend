package com.furnihub.backend.service;

import com.furnihub.backend.dto.AuthResponse;
import com.furnihub.backend.dto.LoginRequest;
import com.furnihub.backend.dto.RegisterRequest;
import com.furnihub.backend.dto.UserDto;
import com.furnihub.backend.entity.JwtToken;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.exception.DuplicateEmailException;
import com.furnihub.backend.exception.DuplicateMobileException;
import com.furnihub.backend.exception.InvalidCredentialsException;
import com.furnihub.backend.repository.JwtTokenRepository;
import com.furnihub.backend.repository.UserRepository;
import com.furnihub.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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
    public UserDto register(RegisterRequest request) {
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

        return UserDto.from(userRepository.save(user));
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);
        saveToken(token, user);

        return new AuthResponse(token, "Bearer", UserDto.from(user));
    }

    private void saveToken(String token, User user) {
        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setTokenType("BEARER");
        jwtToken.setRevoked(false);
        jwtToken.setUser(user);
        jwtTokenRepository.save(jwtToken);
    }
}
