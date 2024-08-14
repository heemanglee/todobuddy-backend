package com.todobuddy.backend.controller;

import com.todobuddy.backend.common.Response;
import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.security.CurrentUser;
import com.todobuddy.backend.service.MemoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping
    public Response<CreateMemoResponse> createMemo(@CurrentUser User user,
        @Valid @RequestBody CreateMemoRequest request) {
        CreateMemoResponse response = memoService.createMemo(user, request);
        return Response.of(HttpStatus.CREATED, response);
    }
}
