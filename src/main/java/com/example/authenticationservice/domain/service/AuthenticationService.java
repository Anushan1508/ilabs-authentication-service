package com.example.authenticationservice.domain.service;

import com.example.authenticationservice.domain.dto.auth.AuthRequest;
import com.example.authenticationservice.domain.dto.auth.AuthResponse;
import com.example.authenticationservice.domain.dto.login.LoginRequest;
import com.example.authenticationservice.domain.dto.login.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);

    AuthResponse autheAdmin(AuthRequest authRequest);

    AuthResponse authUser(AuthRequest authRequest);
}
