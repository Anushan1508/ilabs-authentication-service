package com.example.authenticationservice.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String responseId;
    private String resultCode;
    private String resultDesc;
    private String username;
}
