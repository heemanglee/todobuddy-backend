package com.todobuddy.backend.exception.memo;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MemoAuthorMismatchException extends RuntimeException{

    private final ErrorCode errorCode;
}
