package com.example.authenticationservice.domain.service.impl;

import com.example.authenticationservice.domain.dto.auth.AuthRequest;
import com.example.authenticationservice.domain.dto.auth.AuthResponse;
import com.example.authenticationservice.domain.dto.login.LoginRequest;
import com.example.authenticationservice.domain.dto.login.LoginResponse;
import com.example.authenticationservice.domain.entities.UserEntity;
import com.example.authenticationservice.domain.service.AuthenticationService;
import com.example.authenticationservice.external.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    private String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

    @Autowired
    UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Optional<UserEntity> userEntity;
        try {
            userEntity = userRepository.findByUsernameAndPassword(username, password);
            if (userEntity.isPresent()) {
                String role = userEntity.get().getRole();
                String jwtToken = generateJwtToken(username, role);
                loginResponse.setResponseId(loginRequest.getRequest_id());
                loginResponse.setResultCode("200");
                loginResponse.setResultDesc("Login Success");
                loginResponse.setToken(jwtToken);
            } else {
                loginResponse.setResponseId(loginRequest.getRequest_id());
                loginResponse.setResultCode("401");
                loginResponse.setResultDesc("Login Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResponse;
    }

    @Override
    public AuthResponse autheAdmin(AuthRequest authRequest) {
        String token = authRequest.getToken();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResponseId(authRequest.getRequestId());
        try {
            String username = getUsernameFromToken(token);
            String role = getRoleFromToken(token);

            if (role.equals("admin")) {
                authResponse.setResultCode("200");
                authResponse.setResultDesc("Authenticate Success");
                authResponse.setUsername(username);
            } else {
                authResponse.setResultCode("01");
                authResponse.setResultDesc("Authenticate Failed");
            }
        } catch (Exception e) {
            authResponse.setResultCode("01");
            authResponse.setResultDesc("Token Expired");
        }
        return authResponse;
    }

    @Override
    public AuthResponse authUser(AuthRequest authRequest) {
        String token = authRequest.getToken();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResponseId(authRequest.getRequestId());
        try {
            String username = getUsernameFromToken(token);
            String role = getRoleFromToken(token);
            if (role.equals("user")) {
                authResponse.setResultCode("200");
                authResponse.setResultDesc("Authenticate Success");
                authResponse.setUsername(username);
            } else {
                authResponse.setResultCode("01");
                authResponse.setResultDesc("Authenticate Failed");
            }
        } catch (Exception e) {
            authResponse.setResultCode("01");
            authResponse.setResultDesc("Token Expired");
        }
        return authResponse;
    }

    private String generateJwtToken(String username, String role) {
        String jwtToken = Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return jwtToken;
    }

    private String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    private String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
