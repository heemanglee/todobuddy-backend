package com.todobuddy.backend.controller;

import com.todobuddy.backend.common.Response;
import com.todobuddy.backend.dto.AuthResponse;
import com.todobuddy.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Auth API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Access Token 재발급", description = "Access Token 만료 시에 Refresh Token을 사용하여 새로운 Access Token을 발급하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Access Token 재발급 성공")
    })
    @PostMapping("/reissue")
    public Response<AuthResponse> refreshToken(
        @RequestHeader("Authorization") String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return Response.of(HttpStatus.CREATED, response);
    }
}
