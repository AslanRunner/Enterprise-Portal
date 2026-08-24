package com.aslan.controller;

import com.aslan.dto.DtoPersonelRequest;
import com.aslan.dto.DtoPersonelResponse;
import com.aslan.jwt.AuthRequest;
import com.aslan.jwt.AuthResponse;
import com.aslan.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<DtoPersonelResponse> register(@Valid @RequestBody DtoPersonelRequest request) {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }
}
