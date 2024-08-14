package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public class CreateMemoResponse {

    @Schema(description = "생성된 메모 ID", example = "1")
    private Long memoId;

    @Schema(description = "생성된 메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Nullable
    @Schema(description = "생성된 메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

}
