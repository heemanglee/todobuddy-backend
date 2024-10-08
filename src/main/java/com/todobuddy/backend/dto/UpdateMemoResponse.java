package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메모 수정 Response DTO")
public class UpdateMemoResponse {

    @Schema(description = "수정된 메모 마감일", example = "2024-08-14T00:00:00")
    private LocalDateTime memoDeadLine;

    @Schema(description = "수정된 메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Schema(description = "수정된 메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;
}
