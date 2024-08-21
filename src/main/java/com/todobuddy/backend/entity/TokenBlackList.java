package com.todobuddy.backend.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@RedisHash(value = "token_blacklist", timeToLive = 3600L)
@Getter
public class TokenBlackList {

    @Id
    private String token; // 로그아웃 처리한 Access Token
}
