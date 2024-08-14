package com.todobuddy.backend.common;

import com.todobuddy.backend.exception.category.CategoryErrorCode;
import com.todobuddy.backend.exception.category.CategoryNotFoundException;
import com.todobuddy.backend.exception.category.MaxCategoriesExceededException;
import com.todobuddy.backend.exception.common.CommonErrorCode;
import com.todobuddy.backend.exception.common.EmailSendFailedException;
import com.todobuddy.backend.exception.common.ErrorCode;
import com.todobuddy.backend.exception.common.NotSameVerificationException;
import com.todobuddy.backend.exception.memo.MemoAuthorMismatchException;
import com.todobuddy.backend.exception.memo.MemoErrorCode;
import com.todobuddy.backend.exception.memo.MemoNotFoundException;
import com.todobuddy.backend.exception.memo.MemoStatusUnchangedException;
import com.todobuddy.backend.exception.user.DuplicateEmailException;
import com.todobuddy.backend.exception.user.UserErrorCode;
import com.todobuddy.backend.exception.user.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MaxCategoriesExceededException.class)
    public ResponseEntity<Object> handleMaxCategoriesExceededException(MaxCategoriesExceededException e) {
        ErrorCode errorCode = CategoryErrorCode.MAX_CATEGORIES_EXCEEDED;
        return handleException(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorCode errorCode = CommonErrorCode.ARGUMENT_VALIDATION_FAILED;
        return handleException(errorCode);
    }

    @ExceptionHandler(MemoNotFoundException.class)
    public ResponseEntity<Object> handleMemoNotFoundException(MemoNotFoundException e) {
        ErrorCode errorCode = MemoErrorCode.MEMO_NOT_FOUND;
        return handleException(errorCode);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Object> handleCategoryNotFoundException(CategoryNotFoundException e) {
        ErrorCode errorCode = CategoryErrorCode.CATEGORY_NOT_FOUND;
        return handleException(errorCode);
    }

    @ExceptionHandler(MemoAuthorMismatchException.class)
    public ResponseEntity<Object> handleMemoAuthorMismatchException(MemoAuthorMismatchException e) {
        ErrorCode errorCode = MemoErrorCode.MEMO_AUTHOR_MISMATCH;
        return handleException(errorCode);
    }

    @ExceptionHandler(MemoStatusUnchangedException.class)
    public ResponseEntity<Object> handleMemoStatusUnchangedException(MemoStatusUnchangedException e) {
        ErrorCode errorCode = MemoErrorCode.MEMO_STATUS_UNCHANGED;
        return handleException(errorCode);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
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
