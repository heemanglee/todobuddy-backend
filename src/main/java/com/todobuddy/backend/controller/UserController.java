package com.todobuddy.backend.controller;

import com.todobuddy.backend.common.Response;
import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.dto.GetUserInfoResponse;
import com.todobuddy.backend.dto.LoginRequest;
import com.todobuddy.backend.dto.LoginResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.security.CurrentUser;
import com.todobuddy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "User API")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 회원 가입", description = "사용자 회원 가입 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공")
        }
    )
    public Response<CreateUserResponse> createUser(
        @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse response = userService.createUser(request);
        return Response.of(HttpStatus.CREATED, response);
    }

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보 조회 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공")
        }
    )
    @GetMapping("/me")
    public Response<GetUserInfoResponse> getUserInfo(@CurrentUser User user) {
        GetUserInfoResponse response = userService.getUserInfo(user);
        return Response.of(response);
    }

    @Operation(summary = "로그인", description = "로그인 API", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공")
        }
    )
    @PostMapping("/login")
    public Response<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Response.of(response);
    }
}
