package com.todobuddy.backend.config;

import com.todobuddy.backend.filter.JwtAuthenticationFilter;
import com.todobuddy.backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .cors(cors -> {
                cors.configurationSource(corsConfig.corsConfiguration());
            })
            .csrf().disable()
            .headers(headers -> headers.frameOptions().disable())
            .authorizeHttpRequests(
                authorizeRequest -> {
                    authorizeRequest
                        .requestMatchers("/api/users").permitAll() // 회원가입
                        .requestMatchers("/api/users/login").permitAll() // 로그인
                        .requestMatchers("/swagger-ui/**", "/swagger-resource/**", "/api-docs/**", "/v3/api-docs/**").permitAll() // Swagger
                        .requestMatchers("/api/users/check-email", "/api/users/password").permitAll() // 이메일 검증
                        .anyRequest().authenticated();
                }
            )
            .userDetailsService(customUserDetailsService)
            .sessionManagement(sessionManagement -> {
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptionHandling -> {
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint);
            })
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
