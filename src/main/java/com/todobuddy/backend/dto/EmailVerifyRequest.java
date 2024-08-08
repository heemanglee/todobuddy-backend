package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "아이디/비밀번호 찾기에서 이메일 인증 Request DTO")
public class EmailVerifyRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Schema(description = "사용자가 입력한 이메일", example = "test@test.com")
    private String inputEmail;

}
