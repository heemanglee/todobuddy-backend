package com.todobuddy.backend.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response<T> {

    private int code; // HTTP 상태 코드
    private String message; // HTTP 상태 메시지
    private T data; // 응답 데이터

    public Response(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.message = message;
        this.data = data;
    }


    public static <T> Response<T> of(HttpStatus status, String message, T data) {
        return new Response<>(status, message, data);
    }

    // 200 OK가 아닌 다른 상태 코드를 반환할 때 사용
    public static <T> Response<T> of(HttpStatus status, T data) {
        return Response.of(status, status.name(), data);
    }

    // 200 OK를 반환할 때 사용
    public static <T> Response<T> of(T data) {
        return Response.of(HttpStatus.OK, HttpStatus.OK.name(), data);
    }
}
