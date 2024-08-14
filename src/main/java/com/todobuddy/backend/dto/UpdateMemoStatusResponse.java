package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메모 상태(완료/미완료) 변경 Response DTO")
public class UpdateMemoStatusResponse {

    private String memoStatus;
}
