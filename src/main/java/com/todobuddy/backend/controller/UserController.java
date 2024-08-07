package com.todobuddy.backend.controller;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "사용자 회원 가입", description = "사용자 회원 가입 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "400", description = "중복된 이메일을 사용한 경우 예외 발생")
        }
    )
    public ResponseEntity<CreateUserResponse> createUser(
        @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "사용자 정보 조회", description = "사용자 정보 조회 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "userID로 사용자 조회 실패")
        }
    )
    @GetMapping("/me")
    public ResponseEntity<GetUserInfoResponse> getUserInfo(@CurrentUser User user) {
        log.info("user.id: {}, user.email: {}", user.getId(), user.getEmail());
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @Operation(summary = "로그인", description = "로그인 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "가입되지 않은 이메일 또는 비밀번호가 일치하지 않는 경우 예외 발생")
        }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
