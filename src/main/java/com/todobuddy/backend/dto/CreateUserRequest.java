package com.todobuddy.backend.dto;

import lombok.Getter;

@Getter
public class CreateUserRequest {

    private String email;
    private String password;
    private String nickName;
}
