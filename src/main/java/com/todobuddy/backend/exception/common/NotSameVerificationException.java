package com.todobuddy.backend.exception.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotSameVerificationException extends RuntimeException {

    private final ErrorCode errorCode;
}
