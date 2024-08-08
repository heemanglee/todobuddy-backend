package com.todobuddy.backend.exception.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

// 전역에서 사용되는 ErrorCode
@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "적절하지 않은 입력 값입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
