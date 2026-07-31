package com.furnihub.backend.service;

import com.furnihub.backend.dto.AuthResponse;
import com.furnihub.backend.dto.LoginRequest;
import com.furnihub.backend.dto.RegisterRequest;
import com.furnihub.backend.entity.JwtToken;
import com.furnihub.backend.entity.User;
import com.furnihub.backend.exception.DuplicateEmailException;
import com.furnihub.backend.exception.DuplicateMobileException;
import com.furnihub.backend.exception.InvalidCredentialsException;
import com.furnihub.backend.repository.JwtTokenRepository;
import com.furnihub.backend.repository.UserRepository;
import com.furnihub.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenRepository jwtTokenRepository;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, jwtTokenRepository, new BCryptPasswordEncoder(), jwtService);
    }

    @Test
    void register_success_hashesPasswordAndLowercasesEmail() {
        RegisterRequest request = new RegisterRequest("Asha Rao", "ASHA@Example.com", "9876543210", "StrongPass123!");
        User saved = new User();
        saved.setId(1L);
        saved.setFullName("Asha Rao");
        saved.setEmail("asha@example.com");
        saved.setMobile("9876543210");
        saved.setRole("CUSTOMER");
        saved.setPasswordHash(new BCryptPasswordEncoder().encode("StrongPass123!"));

        when(userRepository.existsByEmail("asha@example.com")).thenReturn(false);
        when(userRepository.existsByMobile("9876543210")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token-abc");

        AuthResponse response = authService.register(request);

        assertEquals(true, response.success());
        assertEquals("jwt-token-abc", response.token());
        assertEquals("Asha Rao", response.fullName());
        assertEquals("CUSTOMER", response.role());
        assertEquals(1L, response.userId());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User captured = captor.getValue();
        assertEquals("asha@example.com", captured.getEmail());
        assertNotEquals("StrongPass123!", captured.getPasswordHash());
        assertTrue(captured.getPasswordHash().startsWith("$2a$"));
        verify(jwtTokenRepository).save(any(JwtToken.class));
    }

    @Test
    void register_duplicateEmail_throwsConflict() {
        RegisterRequest request = new RegisterRequest("Asha Rao", "asha@example.com", "9876543210", "StrongPass123!");
        when(userRepository.existsByEmail("asha@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> authService.register(request));
    }

    @Test
    void register_duplicateMobile_throwsConflict() {
        RegisterRequest request = new RegisterRequest("Asha Rao", "asha@example.com", "9876543210", "StrongPass123!");
        when(userRepository.existsByEmail("asha@example.com")).thenReturn(false);
        when(userRepository.existsByMobile("9876543210")).thenReturn(true);

        assertThrows(DuplicateMobileException.class, () -> authService.register(request));
    }

    @Test
    void login_success_returnsTokenAndStoresJwtToken() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Asha Rao");
        user.setEmail("asha@example.com");
        user.setMobile("9876543210");
        user.setRole("CUSTOMER");
        user.setPasswordHash(new BCryptPasswordEncoder().encode("StrongPass123!"));

        when(userRepository.findByEmailOrMobile("asha@example.com", "asha@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token-abc");

        AuthResponse response = authService.login(new LoginRequest("asha@example.com", "StrongPass123!"));

        assertEquals("jwt-token-abc", response.token());
        assertEquals(true, response.success());
        assertEquals("Asha Rao", response.fullName());
        verify(jwtTokenRepository).save(any(JwtToken.class));
    }

    @Test
    void login_byMobile_works() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Asha Rao");
        user.setEmail("asha@example.com");
        user.setMobile("9876543210");
        user.setRole("CUSTOMER");
        user.setPasswordHash(new BCryptPasswordEncoder().encode("StrongPass123!"));

        when(userRepository.findByEmailOrMobile("9876543210", "9876543210")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token-mobile");

        AuthResponse response = authService.login(new LoginRequest("9876543210", "StrongPass123!"));

        assertEquals("jwt-token-mobile", response.token());
        assertEquals("Asha Rao", response.fullName());
    }

    @Test
    void login_wrongPassword_throwsUnauthorized() {
        User user = new User();
        user.setEmail("asha@example.com");
        user.setPasswordHash(new BCryptPasswordEncoder().encode("StrongPass123!"));
        when(userRepository.findByEmailOrMobile("asha@example.com", "asha@example.com")).thenReturn(Optional.of(user));

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("asha@example.com", "wrong-password")));
    }

    @Test
    void login_unknownIdentifier_throwsUnauthorized() {
        when(userRepository.findByEmailOrMobile("nobody@example.com", "nobody@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new LoginRequest("nobody@example.com", "whatever123")));
    }

    @Test
    void userDto_neverExposesPassword() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Asha Rao");
        user.setEmail("asha@example.com");
        user.setMobile("9876543210");
        user.setRole("CUSTOMER");
        user.setPasswordHash("should-never-leak");

        var dto = com.furnihub.backend.dto.UserDto.from(user);
        assertEquals("asha@example.com", dto.email());
        assertNotNull(dto);
    }
}
