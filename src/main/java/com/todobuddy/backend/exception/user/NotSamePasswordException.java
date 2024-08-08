package com.todobuddy.backend.exception.user;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotSamePasswordException extends RuntimeException{

    private final ErrorCode errorCode;
}
