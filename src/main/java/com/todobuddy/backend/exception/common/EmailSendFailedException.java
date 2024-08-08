package com.todobuddy.backend.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailSendFailedException extends RuntimeException{

    private final ErrorCode errorCode;
}
