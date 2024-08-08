package com.todobuddy.backend.mail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EmailMessage {

    private String to; // 수신자
    private String subject; // 메일 제목
    private String message; // 메일 내용
}
