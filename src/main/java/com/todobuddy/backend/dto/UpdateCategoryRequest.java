package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "카테고리 수정 Request DTO")
public class UpdateCategoryRequest {

    @Schema(description = "카테고리 이름", example = "운동")
    private String categoryName;
}
