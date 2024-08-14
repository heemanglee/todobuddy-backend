package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateMemoResponse {

    @Schema(description = "생성된 메모 ID", example = "1")
    private Long memoId;

    @Schema(description = "생성된 메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Schema(description = "생성된 메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @Schema(description = "생성된 메모 마감 기한", example = "2024-08-14T14:40:00")
    private LocalDateTime memoDeadLine;

}
