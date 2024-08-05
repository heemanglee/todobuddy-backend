package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 사용자 등록
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        User createUser = User.builder()
            .email(request.getEmail())
            .password(request.getPassword())
            .nickName(request.getNickName())
            .build();

        User savedUser = userRepository.save(createUser);

        return CreateUserResponse.of(savedUser);
    }
}
