package com.todobuddy.backend.service;

import com.todobuddy.backend.dto.AuthResponse;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.exception.jwt.JwtErrorCode;
import com.todobuddy.backend.exception.jwt.TokenExpiredException;
import com.todobuddy.backend.exception.user.UserErrorCode;
import com.todobuddy.backend.exception.user.UserNotFoundException;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        try {
            String extractBearerToken = extractBearerToken(refreshToken);
            Claims claims = jwtTokenProvider.validateToken(extractBearerToken); // Refresh Token 유효성 검사
            String email = claims.getSubject(); // Refresh Token에서 이메일 추출

            User findUser = findUserByEmail(email);

            String newAccessToken = jwtTokenProvider.generateAccessToken(findUser); // 새로운 Access Token 발급
            return new AuthResponse(newAccessToken, extractBearerToken, "Bearer");
        } catch (ExpiredJwtException e) { // Refresh Token 만료
            throw new TokenExpiredException(JwtErrorCode.EXPIRED_TOKEN);
        }
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
    }

    // Bearer Token 추출
    private String extractBearerToken(String token) {
        // Bearer eyJhbGciOiJIUzI1NiJ9.. -> eyJhbGciOiJIUzI1NiJ9..
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }
}
