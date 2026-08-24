package com.aslan.service.impl;

import com.aslan.dto.DtoPersonelRequest;
import com.aslan.dto.DtoPersonelResponse;
import com.aslan.jwt.AuthRequest;
import com.aslan.jwt.AuthResponse;
import com.aslan.jwt.JwtService;
import com.aslan.service.AuthService;
import com.aslan.service.PersonelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PersonelService personelService;

    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);
        
        AuthResponse response = new AuthResponse();
        response.setAccessToken(token);
        response.setTokenType("Bearer");
        return response;
    }

    @Override
    public DtoPersonelResponse register(DtoPersonelRequest request) {
        return personelService.createPersonel(request);
    }
}
