package com.todobuddy.backend.exception.memo;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MemoErrorCode implements ErrorCode {

    MEMO_NOT_FOUND(HttpStatus.NOT_FOUND, "메모를 찾을 수 없습니다."),
    MEMO_AUTHOR_MISMATCH(HttpStatus.FORBIDDEN, "메모 작성자가 아닙니다."),
    MEMO_STATUS_UNCHANGED(HttpStatus.BAD_REQUEST, "동일한 메모 상태로 변경할 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
