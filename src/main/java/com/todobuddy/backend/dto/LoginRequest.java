package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "로그인 Request DTO")
public class LoginRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Schema(description = "사용자 비밀번호", example = "test")
    private String password;
}
