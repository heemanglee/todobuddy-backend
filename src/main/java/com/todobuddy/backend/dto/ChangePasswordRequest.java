package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "비밀번호 변경 Request DTO")
public class ChangePasswordRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Schema(description = "새로운 비밀번호를 설정할 이메일", example = "test@test.com")
    private String email;

    @NotNull(message = "인증 코드를 입력하세요.")
    @Schema(description = "인증 코드 4자리", example = "1234")
    private Integer verificationCode;

    @NotBlank(message = "새로운 비밀번호를 입력하세요.")
    @Schema(description = "새로운 비밀번호", example = "password1234")
    private String password;

    @NotBlank(message = "2차 확인을 위한 비밀번호를 입력하세요.")
    @Schema(description = "비밀번호 2차 확인", example = "password1234")
    private String confirmPassword;
}
