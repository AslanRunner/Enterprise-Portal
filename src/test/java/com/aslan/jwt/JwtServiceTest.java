package com.aslan.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "5N+6yAw9UJlZGIE3ivXxkQlxnb9BauSkvcdSJ447DQE=");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 7_200_000L);
    }

    @Test
    void generateToken_shouldUseUserEmailAsSubject() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertEquals("test@example.com", jwtService.getUsernameByToken(token));
    }

    @Test
    void isTokenValid_shouldReturnTrueForSameUser() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUser() {
        UserDetails tokenOwner = User.withUsername("owner@example.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();
        UserDetails otherUser = User.withUsername("other@example.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(tokenOwner);

        assertFalse(jwtService.isTokenValid(token, otherUser));
    }
}
