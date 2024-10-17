package ru.sessionservice.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private Object userResponse;
}
