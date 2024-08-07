package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.dto.GetUserInfoResponse;
import com.todobuddy.backend.dto.LoginRequest;
import com.todobuddy.backend.dto.LoginResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 사용자 등록
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {

        validateExistUserEmail(request.getEmail()); // 이메일은 중복하여 가입될 수 없다.

        User createUser = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .nickName(request.getNickName())
            .build();

        User savedUser = userRepository.save(createUser);

        return CreateUserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public GetUserInfoResponse getUserInfo(User user) {
        User findUser = findUserById(user.getId());
        log.info("findUser: {}", findUser);
        return GetUserInfoResponse.of(findUser);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User findUser = findUserByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), findUser.getPassword())) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않습니다.");
        }

        String createJwtToken = jwtTokenProvider.generateToken(findUser);
        return new LoginResponse(createJwtToken);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(
                () -> new IllegalArgumentException("가입되지 않은 이메일입니다. email=" + email));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + userId));
    }

    private void validateExistUserEmail(String email) {
        userRepository.findByEmail(email)
            .ifPresent(user -> {
                throw new IllegalArgumentException("이미 가입된 이메일입니다. email=" + email);
            });
    }
}
