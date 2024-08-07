package com.todobuddy.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "사용자 회원가입 Request DTO")
public class CreateUserRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Schema(description = "사용자 비밀번호", example = "test")
    private String password;

    @NotBlank(message = "닉네임을 입력하세요.")
    @Schema(description = "사용자 닉네임", example = "test")
    private String nickName;
}
