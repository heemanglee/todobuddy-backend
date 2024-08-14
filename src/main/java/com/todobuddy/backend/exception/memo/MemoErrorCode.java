package com.todobuddy.backend.exception.memo;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MemoErrorCode implements ErrorCode {

    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "메모를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
