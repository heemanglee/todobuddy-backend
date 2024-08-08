package com.todobuddy.backend.controller;

import com.todobuddy.backend.common.Response;
import com.todobuddy.backend.dto.CreateCategoryRequest;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.security.CurrentUser;
import com.todobuddy.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Category", description = "Category API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 등록", description = "카테고리 등록 API")
    @ApiResponses(
        value = {
            @ApiResponse(responseCode = "201", description = "카테고리 등록 성공")
        }
    )
    @PostMapping
    public Response<Void> createCategory(@CurrentUser User user,
        @RequestBody CreateCategoryRequest request) {
        categoryService.createCategory(user, request);
        return Response.of(HttpStatus.CREATED, null);
    }
}
