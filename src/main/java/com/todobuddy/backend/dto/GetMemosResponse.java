package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(description = "메모 조회 Response DTO")
public class GetMemosResponse {

    @Schema(description = "카테고리 이름", example = "토익")
    private String categoryName;

    @Schema(description = "카테고리 순서", example = "1 (1부터 시작)")
    private int categoryOrder;

    @Schema(description = "메모 내용", example = "토익 공부")
    private String memoContent;

    @Schema(description = "메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @Schema(description = "메모 마감 일정", example = "2024-08-16T23:59:59")
    private LocalDateTime memoDeadLine;
}
