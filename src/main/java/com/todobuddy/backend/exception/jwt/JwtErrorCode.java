package com.todobuddy.backend.exception.jwt;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum JwtErrorCode implements ErrorCode {

    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
