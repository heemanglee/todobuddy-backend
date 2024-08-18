package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Schema(description = "메모 수정 Request DTO")
public class UpdateMemoRequest {

    @Nullable
    @Schema(description = "메모 마감일", example = "2024-08-14T00:00:00")
    private LocalDateTime memoDeadLine;

    @NotBlank
    @Schema(description = "메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Nullable
    @Schema(description = "메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @NotBlank
    @Schema(description = "카테고리 이름", example = "토익")
    private String categoryName;
}
