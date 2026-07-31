package com.furnihub.backend.security;

import com.furnihub.backend.config.JwtProperties;
import com.furnihub.backend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-key-that-is-long-enough-for-hs256-0123456789abcdef");
        properties.setExpirationMs(86_400_000L);
        jwtService = new JwtService(properties);

        user = new User();
        user.setId(1L);
        user.setEmail("asha@example.com");
        user.setRole("CUSTOMER");
    }

    @Test
    void generateToken_returnsNonEmptyToken() {
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractEmail_returnsSubjectEmail() {
        String token = jwtService.generateToken(user);
        assertEquals("asha@example.com", jwtService.extractEmail(token));
    }

    @Test
    void isValid_acceptsTokenOfSameUser() {
        String token = jwtService.generateToken(user);
        assertTrue(jwtService.isValid(token, user));
    }

    @Test
    void isValid_rejectsTokenOfDifferentUser() {
        String token = jwtService.generateToken(user);
        User other = new User();
        other.setEmail("other@example.com");
        assertFalse(jwtService.isValid(token, other));
    }
}
