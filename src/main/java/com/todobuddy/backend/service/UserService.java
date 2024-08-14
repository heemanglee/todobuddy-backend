package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.ChangePasswordRequest;
import com.todobuddy.backend.dto.CreateUserRequest;
import com.todobuddy.backend.dto.CreateUserResponse;
import com.todobuddy.backend.dto.EmailVerifyRequest;
import com.todobuddy.backend.dto.GetUserInfoResponse;
import com.todobuddy.backend.dto.LoginRequest;
import com.todobuddy.backend.dto.LoginResponse;
import com.todobuddy.backend.entity.Category;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.entity.VerificationCode;
import com.todobuddy.backend.exception.common.CommonErrorCode;
import com.todobuddy.backend.exception.common.NotSameVerificationException;
import com.todobuddy.backend.exception.user.DuplicateEmailException;
import com.todobuddy.backend.exception.user.UserErrorCode;
import com.todobuddy.backend.exception.user.UserNotFoundException;
import com.todobuddy.backend.repository.CategoryRepository;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.repository.VerificationCodeRepository;
import com.todobuddy.backend.security.jwt.JwtTokenProvider;
import java.util.List;
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
    private final VerificationCodeRepository verificationCodeRepository;
    private final CategoryRepository categoryRepository;

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

    @Transactional(readOnly = true)
    public void isExistUserEmail(EmailVerifyRequest request) {
        findUserByEmail(request.getInputEmail());
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {

        // 1차 비밀번호와 2차 비밀번호가 일치하는지 확인
        if(!request.getPassword().equals(request.getConfirmPassword())) {
            throw new NotSameVerificationException(UserErrorCode.NOT_SAME_PASSWORD);
        }

        // redis에 인증 코드를 할당받은 이메일이 존재하는지 확인
        VerificationCode findVerificationCode = verificationCodeRepository.findById(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        // 인증 코드가 일치하는지 확인
        if(!findVerificationCode.getVerificationCode().equals(request.getVerificationCode()))  {
            throw new NotSameVerificationException(CommonErrorCode.NOT_SAME_VERIFICATION);
        }

        // 비밀번호 변경
        User findUser = findUserByEmail(request.getEmail());
        findUser.updatePassword(passwordEncoder.encode(request.getPassword()));

        // redis에 저장된 인종 코드 삭제
        verificationCodeRepository.delete(findVerificationCode);
    }

    @Transactional
    public void deleteUser(User user) {
        List<Category> categories = categoryRepository.findByUser(user);
        List<Long> categoryIds = categories.stream()
            .map(Category::getId)
            .toList();

        categoryRepository.deleteAllByIdInQuery(categoryIds); // 사용자가 작성한 모든 카테고리 삭제
        userRepository.delete(user);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }

    private void validateExistUserEmail(String email) {
        userRepository.findUserByIdInQuery(email)
            .ifPresent(user -> {
                throw new DuplicateEmailException(UserErrorCode.DUPLICATE_EMAIL);
            });
    }
}
