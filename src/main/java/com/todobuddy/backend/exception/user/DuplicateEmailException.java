package com.todobuddy.backend.exception.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DuplicateEmailException extends RuntimeException {

    private final UserErrorCode errorCode;
}
