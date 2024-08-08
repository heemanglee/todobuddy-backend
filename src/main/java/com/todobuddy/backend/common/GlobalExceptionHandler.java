package com.todobuddy.backend.common;

import com.todobuddy.backend.exception.common.CommonErrorCode;
import com.todobuddy.backend.exception.common.EmailSendFailedException;
import com.todobuddy.backend.exception.common.ErrorCode;
import com.todobuddy.backend.exception.common.NotSameVerificationException;
import com.todobuddy.backend.exception.user.DuplicateEmailException;
import com.todobuddy.backend.exception.user.UserErrorCode;
import com.todobuddy.backend.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(createErrorResponse(errorCode));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        ErrorCode errorCode = UserErrorCode.USER_NOT_FOUND;
        return handleException(errorCode);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorCode errorCode = UserErrorCode.DUPLICATE_EMAIL;
        return handleException(errorCode);
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<Object> handleEmailSendFailedException(EmailSendFailedException e) {
        ErrorCode errorCode = CommonErrorCode.EMAIL_SEND_FAILED;
        return handleException(errorCode);
    }

    @ExceptionHandler(NotSameVerificationException.class)
    public ResponseEntity<Object> handleNotSameVerificationException(NotSameVerificationException e) {
        ErrorCode errorCode = CommonErrorCode.NOT_SAME_VERIFICATION;
        return handleException(errorCode);
    }

    private ResponseEntity<Object> handleException(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(createErrorResponse(errorCode));
    }

    private ErrorResponse createErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();
    }
}
