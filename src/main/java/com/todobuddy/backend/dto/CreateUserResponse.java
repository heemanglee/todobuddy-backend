package com.todobuddy.backend.dto;

import com.todobuddy.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateUserResponse {

    private Long userId;
    private String email;
    private String password;
    private String nickName;

    public static CreateUserResponse of(User user) {
        return new CreateUserResponse(user.getId(), user.getEmail(), user.getPassword(),
            user.getNickName());
    }
}
