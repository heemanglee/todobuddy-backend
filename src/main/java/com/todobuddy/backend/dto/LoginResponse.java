package com.todobuddy.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {

    private AuthResponse jwtToken;
}
