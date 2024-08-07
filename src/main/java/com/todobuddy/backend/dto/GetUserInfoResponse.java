package com.todobuddy.backend.dto;

import com.todobuddy.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetUserInfoResponse {

    private String email;
    private String nickName;
    private String role;

    public static GetUserInfoResponse of(User user) {
        return new GetUserInfoResponse(user.getEmail(), user.getNickName(), user.getRole().name());
    }
}
