package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "메모 조회 Request DTO")
public class GetMemosRequest {

    @Schema(description = "메모 조회 년도", example = "2024")
    private Integer year;

    @Schema(description = "메모 조회 월", example = "8")
    private Integer month;

}
