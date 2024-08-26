package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "카테고리 조회 Response DTO")
public class GetCategoriesResponse {

    @Schema(description = "카테고리 ID", example = "1")
    private Long id; //

    @Schema(description = "카테고리 이름", example = "토익")
    private String categoryName;

    @Schema(description = "카테고리 순서", example = "(1, 2, 3) 중 하나")
    private int categoryId;
}
