package com.todobuddy.backend.controller;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.dto.GetUserInfoResponse;
import com.todobuddy.backend.dto.LoginRequest;
import com.todobuddy.backend.dto.LoginResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.security.CurrentUser;
import com.todobuddy.backend.service.UserService;
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
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(
        @RequestBody CreateUserRequest request
    ) {
        CreateUserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserInfoResponse> getUserInfo(@CurrentUser User user) {
        log.info("user.id: {}, user.email: {}", user.getId(), user.getEmail());
        return ResponseEntity.ok(userService.getUserInfo(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
