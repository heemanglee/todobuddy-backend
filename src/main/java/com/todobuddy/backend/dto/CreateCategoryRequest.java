package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "카테고리 등록 Request DTO")
public class CreateCategoryRequest {

    @NotBlank(message = "카테고리 이름을 입력하세요.")
    @Schema(description = "카테고리 이름", example = "토익")
    private String categoryName;

    @NotNull(message = "카테고리 순서를 입력하세요.")
    @Schema(description = "카테고리 순서", example = "1")
    private Integer categoryOrder;
}
