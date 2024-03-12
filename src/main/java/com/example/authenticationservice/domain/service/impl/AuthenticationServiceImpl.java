package com.example.authenticationservice.domain.service.impl;

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
    @Autowired
    UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        System.out.println("loginRequest From AuthService | Auth Service = " + loginRequest);
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Optional<UserEntity> userEntity;
        try {
            userEntity = userRepository.findByUsernameAndPassword(username, password);
            System.out.println("userEntity = " + userEntity);
            if (userEntity.isPresent()) {
                String role = userEntity.get().getRole();
                String jwtToken = generateJwtToken(username, role);
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setResponseId(loginRequest.getRequest_id());
                loginResponse.setResultCode("00");
                loginResponse.setResultDesc("Login Success");
                loginResponse.setToken(jwtToken);
                return loginResponse;
            } else {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setResponseId(loginRequest.getRequest_id());
                loginResponse.setResultCode("01");
                loginResponse.setResultDesc("Login Failed");
                return loginResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateJwtToken(String username, String role) {
        byte[] secretKeyBytes = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
        String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

        String jwtToken = Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return jwtToken;
    }
}
