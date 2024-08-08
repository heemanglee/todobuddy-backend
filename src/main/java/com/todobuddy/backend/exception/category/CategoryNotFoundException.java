package com.todobuddy.backend.exception.category;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CategoryNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;
}
