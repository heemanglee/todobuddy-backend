package com.todobuddy.backend.filter;

import com.todobuddy.backend.entity.TokenBlackList;
import com.todobuddy.backend.entity.User;
import com.todobuddy.backend.repository.TokenBlackListRepository;
import com.todobuddy.backend.repository.UserRepository;
import com.todobuddy.backend.security.CustomUserDetails;
import com.todobuddy.backend.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TokenBlackListRepository tokenBlackListRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = parseBearToken(request);

        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // BlackList에 Access Token이 저장되어 있는지 확인한다.
        TokenBlackList tokenBlackList = tokenBlackListRepository.findById(jwtToken).orElse(null);
        if (tokenBlackList != null) {
            throw new InsufficientAuthenticationException("로그아웃된 토큰입니다.");
        }

        try {
            CustomUserDetails user = parseToken(jwtToken);

            UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
                user, "", user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authenticated);

            filterChain.doFilter(request, response);
        } catch(ExpiredJwtException e) {
            throw new InsufficientAuthenticationException("토큰이 만료되었습니다.");
        }

    }

    private CustomUserDetails parseToken(String jwtToken) {
        if (jwtToken == null) {
            return null;
        }

        Claims claims = jwtTokenProvider.validateToken(jwtToken);
        if (claims == null) {
            return null;
        }

        String email = claims.getSubject();

        User findUser = findUserByEmail(email); // GET /users/me에서 findById()를 사용하기 위해 User 조회가 필요함.

        return new CustomUserDetails(findUser);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseGet(() -> userRepository.save(User.builder().email(email).build()));
    }

    private String parseBearToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Optional.ofNullable(authorization)
            .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
            .map(token -> token.substring(7))
            .orElse(null);
    }
}
