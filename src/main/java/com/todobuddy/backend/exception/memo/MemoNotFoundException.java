package com.todobuddy.backend.exception.memo;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
}
