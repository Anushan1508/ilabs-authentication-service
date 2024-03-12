package com.example.authenticationservice.domain.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String request_id;
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
