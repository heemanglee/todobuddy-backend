package com.todobuddy.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

@Getter
@Builder
@RequiredArgsConstructor
public class ErrorResponse {

    private final String code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // errors가 null이면 응답에 포함하지 않음
    private final List<ValidatorError> errors;

    @Getter
    @Builder
    @RequiredArgsConstructor
    static class ValidatorError { // @Valid 검증 실패 시 응답에 담을 에러 정보

        private final String field;
        private final String message;

        public static ValidatorError of(final FieldError fieldError) {
            return ValidatorError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
        }
    }
}
