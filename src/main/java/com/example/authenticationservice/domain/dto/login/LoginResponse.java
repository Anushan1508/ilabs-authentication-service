package com.example.authenticationservice.domain.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String responseId;
    @JsonProperty("resultCode")
    private String resultCode;
    @JsonProperty("resultDesc")
    private String  resultDesc;
    private String token;
}

