package com.todobuddy.backend.exception.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArgumentValidationException extends RuntimeException {

    private ErrorCode errorCode;
}
