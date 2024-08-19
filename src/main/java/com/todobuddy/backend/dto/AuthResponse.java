package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "인증 응답 DTO")
public class AuthResponse {

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0b2RvYnVkZHkifQ....")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJiUzI1NiJ9.eyJzdWIiOiJ0b2RvYnVkZHkifQ....")
    private String refreshToken;

    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType;
}
