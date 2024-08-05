package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 사용자 등록
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {

        validateExistUserEmail(request); // 이메일은 중복하여 가입될 수 없다.

        User createUser = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .nickName(request.getNickName())
            .build();

        User savedUser = userRepository.save(createUser);

        return CreateUserResponse.of(savedUser);
    }

    private void validateExistUserEmail(CreateUserRequest request) {
        userRepository.findByEmail(request.getEmail())
            .ifPresent(user -> {
                throw new IllegalArgumentException("이미 가입된 이메일입니다. email=" + request.getEmail());
            });
    }
}
