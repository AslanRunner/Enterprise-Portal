package com.aslan.service;

import com.aslan.dto.DtoPersonelRequest;
import com.aslan.dto.DtoPersonelResponse;
import com.aslan.jwt.AuthRequest;
import com.aslan.jwt.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    DtoPersonelResponse register(DtoPersonelRequest request);
}
