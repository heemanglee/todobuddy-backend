package com.todobuddy.backend.exception.category;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CategoryErrorCode implements ErrorCode {

    MAX_CATEGORIES_EXCEEDED(HttpStatus.BAD_REQUEST, "카테고리 등록은 최대 3개까지 가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
