package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@Schema(description = "메모 작성 Request DTO")
public class CreateMemoRequest {

    @NotBlank(message = "메모 제목은 필수 입력 값입니다.")
    @Schema(description = "메모 내용", example = "토익 공부하기")
    private String memoContent;

    @Nullable
    @Schema(description = "링크", example = "https://www.todobuddy.com")
    private String memoLink;

    @Nullable
    @Schema(description = "마감 기한", example = "2024-08-14T14:40:00")
    private LocalDateTime memoDeadLine;

    @NotEmpty(message = "카테고리 ID는 필수 입력 값입니다.")
    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;
}
