package com.todobuddy.backend.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@RedisHash(value = "verification_code", timeToLive = 60L * 10)
@Getter
public class VerificationCode {

    @Id
    private String email; // 인증 코드를 전송한 이메일

    private String verificationCode; // 인증 코드
}
