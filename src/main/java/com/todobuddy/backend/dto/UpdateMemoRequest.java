package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Schema(description = "메모 수정 Request DTO")
public class UpdateMemoRequest {

    @Nullable
    @Schema(description = "메모 마감일", example = "2024-08-14T00:00:00")
    private LocalDateTime memoDeadLine;

    @NotBlank(message = "메모 내용은 필수 입력 값입니다.")
    @Schema(description = "메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Nullable
    @Schema(description = "메모 링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @NotNull(message = "카테고리 ID는 필수 입력 값입니다.")
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;
}
