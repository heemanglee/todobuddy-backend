package com.todobuddy.backend.util;

import com.todobuddy.backend.entity.User;

public class TestUtils {

    public static User createUser(String email, String password, String nickName) {
        return User.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .build();
    }
}
