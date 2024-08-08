package com.todobuddy.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.dto.LoginRequest;
import com.todobuddy.backend.dto.LoginResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.user.DuplicateEmailException;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.security.jwt.JwtTokenProvider;
import com.todobuddy.backend.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Spy
    BCryptPasswordEncoder passwordEncoder; // 실제 빈을 주입받아 사용
    @Mock
    JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("사용자 등록 테스트")
    void createUserTest() {
        // given
        String email = "test@test.com";
        String nickName = "nickName";
        String rawPassword = "rawPassword"; // 사용자가 입력한 비밀번호
        String encodedPassword = passwordEncoder.encode(rawPassword); // 암호화된 비밀번호

        User user = TestUtils.createUser(email, encodedPassword, nickName);
        when(userRepository.save(any())).thenReturn(user);

        CreateUserRequest request = new CreateUserRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", rawPassword);
        ReflectionTestUtils.setField(request, "nickName", nickName);

        // when
        CreateUserResponse response = userService.createUser(request);

        // then
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getNickName()).isEqualTo(nickName);
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입을 할 수 없다.")
    void createUserWithDuplicatedEmailTest() {
        // given
        String duplicatedEmail = "test@test.com";
        String nickName = "nickName";
        String rawPassword = "rawPassword"; // 사용자가 입력한 비밀번호
        String encodedPassword = passwordEncoder.encode(rawPassword); // 암호화된 비밀번호

        CreateUserRequest request = new CreateUserRequest();
        ReflectionTestUtils.setField(request, "email", duplicatedEmail);
        ReflectionTestUtils.setField(request, "password", rawPassword);
        ReflectionTestUtils.setField(request, "nickName", nickName);

        User existUser = TestUtils.createUser(duplicatedEmail, encodedPassword, nickName);
        when(userRepository.findByEmail(duplicatedEmail)).thenReturn(Optional.of(existUser));

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request)); // 중복된 이메일로 가입 시도 -> 예외 발생

        verify(userRepository).findByEmail(duplicatedEmail); // 호출 여부 확인
    }

    @Test
    @DisplayName("DB에 이메일이 존재하면 로그인을 정상적으로 수행한다.")
    void loginProcessSuccessTest() {
        // given
        String email = "test@test.com";
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String nickName = "test";

        User createUser = TestUtils.createUser(email, encodedPassword, nickName);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(createUser));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        String jwtToken = "jwtToken";
        when(jwtTokenProvider.generateToken(createUser)).thenReturn(jwtToken);

        // when
        LoginRequest request = new LoginRequest();
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "test");

        // then
        LoginResponse response = userService.login(request);
        assertThat(response.getAccessToken()).isEqualTo(jwtToken);
    }

    @Test
    @DisplayName("DB에 사용자가 존재하지 않으면 예외가 발생한다.")
    void loginProcessFailTest() {
        // given
        String email = "testA@test.com";
        String rawPassword = "test";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        String nickName = "test";

        User createUser = TestUtils.createUser(email, encodedPassword, nickName);
        when(userRepository.findByEmail(email)).thenThrow(new IllegalArgumentException());

        // when
        LoginRequest request = new LoginRequest();
        ReflectionTestUtils.setField(request, "email", email);
        ReflectionTestUtils.setField(request, "password", "test");

        // then
        assertThrows(IllegalArgumentException.class, () -> userService.login(request));
    }

}