package com.todobuddy.backend.controller;

import com.todobuddy.backend.common.Response;
import com.todobuddy.backend.dto.CreateMemoRequest;
import com.todobuddy.backend.dto.CreateMemoResponse;
import com.todobuddy.backend.dto.UpdateMemoRequest;
import com.todobuddy.backend.dto.UpdateMemoResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.security.CurrentUser;
import com.todobuddy.backend.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memos")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@Tag(name = "Memo", description = "Memo API")
public class MemoController {

    private final MemoService memoService;

    @Operation(summary = "메모 작성", description = "메모 작성 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "메모 작성 성공")
    })
    @PostMapping
    public Response<CreateMemoResponse> createMemo(@CurrentUser User user,
        @Valid @RequestBody CreateMemoRequest request) {
        CreateMemoResponse response = memoService.createMemo(user, request);
        return Response.of(HttpStatus.CREATED, response);
    }

    @Operation(summary = "메모 수정", description = "메모 수정 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메모 수정 성공")
    })
    @PatchMapping("/{memoId}")
    public Response<UpdateMemoResponse> updateMemo(@CurrentUser User user,
        @PathVariable("memoId") Long memoId, @Valid @RequestBody UpdateMemoRequest request) {
        UpdateMemoResponse response = memoService.updateMemo(user, memoId, request);
        return Response.of(response);
    }
}
