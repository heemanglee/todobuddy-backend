package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카테고리 생성 Response DTO")
public class CreateCategoryResponse {

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "카테고리 이름", example = "토익")
    private String categoryName;

    @Schema(description = "카테고리 순서", example = "1 (1~3까지)")
    private int categoryOrderId;

}
