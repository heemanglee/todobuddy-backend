package com.todobuddy.backend.exception.jwt;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenExpiredException extends RuntimeException {

    private final ErrorCode errorCode;
}
