package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메모 상태(완료/미완료) 변경 Response DTO")
public class UpdateMemoStatusResponse {

    @Schema(description = "메모 ID", example = "1")
    private Long memoId;

    @Schema(description = "메모 상탸", example = "COMPLETED 또는 NOT_COMPLETED")
    private String memoStatus;
}
