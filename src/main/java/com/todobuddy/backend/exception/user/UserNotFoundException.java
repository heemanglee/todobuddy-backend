package com.todobuddy.backend.exception.user;

import com.todobuddy.backend.exception.common.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;
}
