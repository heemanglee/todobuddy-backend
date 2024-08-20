package com.todobuddy.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todobuddy.backend.common.ErrorResponse;
import com.todobuddy.backend.exception.common.ErrorCode;
import com.todobuddy.backend.exception.jwt.JwtErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorCode errorCode = JwtErrorCode.EXPIRED_TOKEN;
        ErrorResponse errorResponse = ErrorResponse.builder()
            .code(errorCode.name())
            .message(errorCode.getMessage())
            .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }

}
