package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "생성된 인증 코드 Response DTO")
public class VerifyCodeResponse {

    @Schema(description = "생성된 인증 코드(숫자 4개)", example = "1234")
    private int verifyCode;
}
