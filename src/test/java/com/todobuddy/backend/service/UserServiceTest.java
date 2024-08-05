package com.todobuddy.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.UserRepository;
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
        assertThat(passwordEncoder.matches(rawPassword, response.getPassword())).isTrue();
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입을 할 수 없다.")
    void createUserWithDuplicatedEmail() {
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

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request)); // 중복된 이메일로 가입 시도 -> 예외 발생

        verify(userRepository).findByEmail(duplicatedEmail); // 호출 여부 확인
    }
}