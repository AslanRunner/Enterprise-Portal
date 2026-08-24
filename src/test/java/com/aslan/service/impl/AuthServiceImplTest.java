package com.aslan.service.impl;

import com.aslan.jwt.AuthRequest;
import com.aslan.jwt.AuthResponse;
import com.aslan.jwt.JwtService;
import com.aslan.service.PersonelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PersonelService personelService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_shouldAuthenticateWithEmailAndReturnBearerToken() {
        AuthRequest request = new AuthRequest("test@example.com", "secret123");
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("encoded-password")
                .authorities("ROLE_USER")
                .build();

        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        AuthResponse response = authService.login(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor =
                ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        verify(userDetailsService).loadUserByUsername("test@example.com");
        verify(jwtService).generateToken(userDetails);

        assertEquals("test@example.com", authCaptor.getValue().getPrincipal());
        assertEquals("secret123", authCaptor.getValue().getCredentials());
        assertEquals("jwt-token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
    }
}
