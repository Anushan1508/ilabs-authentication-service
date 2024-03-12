package com.example.authenticationservice.application.controller;

import com.example.authenticationservice.domain.dto.login.LoginRequest;
import com.example.authenticationservice.domain.dto.login.LoginResponse;
import com.example.authenticationservice.domain.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base-url-context}")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/hello")
    public String hello() {
        return "Hello AuthenticationController";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("loginRequest From Controller | Auth Service = " + loginRequest);
        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

}
