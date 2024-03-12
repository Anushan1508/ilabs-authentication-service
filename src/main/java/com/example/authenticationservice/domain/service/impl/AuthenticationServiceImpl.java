package com.example.authenticationservice.domain.service.impl;

import com.example.authenticationservice.domain.dto.auth.AuthRequest;
import com.example.authenticationservice.domain.dto.auth.AuthResponse;
import com.example.authenticationservice.domain.dto.login.LoginRequest;
import com.example.authenticationservice.domain.dto.login.LoginResponse;
import com.example.authenticationservice.domain.entities.UserEntity;
import com.example.authenticationservice.domain.service.AuthenticationService;
import com.example.authenticationservice.external.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String SECRET_KEY = "your_secret_key_here";

    @Autowired
    UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Optional<UserEntity> userEntity;
        try {
            userEntity = userRepository.findByUsernameAndPassword(username, password);
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

    @Override
    public AuthResponse authendicate(AuthRequest authRequest) {
        String token = authRequest.getToken();
        AuthResponse authResponse = new AuthResponse();
        authResponse.setResponseId(authRequest.getRequestId());
        try {
//            String username = Jwts.parserBuilder()
//                    .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS256))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .get("username", String.class);
//
//            String role = Jwts.parserBuilder()
//                    .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS256))
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody()
//                    .get("role", String.class);
//            Claims claims = Jwts.parser()
//                    .setSigningKey(SECRET_KEY)
//                    .parseClaimsJws(authRequest.getToken())
//                    .getBody();
//
//            String username = claims.get("username", String.class);
//            String role = claims.get("role", String.class);
            String username = "user";
            String role = "admin";

            System.out.println("username = " + username);
            System.out.println("role = " + role);
            if (role.equals("admin")) {
                authResponse.setResultCode("200");
                authResponse.setResultDesc("Authenticate Success");
            } else {
                authResponse.setResultCode("01");
                authResponse.setResultDesc("Authenticate Failed");
            }
            authResponse.setUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authResponse;
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
