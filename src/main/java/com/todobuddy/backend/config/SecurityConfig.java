package com.todobuddy.backend.config;

import com.todobuddy.backend.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .csrf().disable()
            .headers(
                headerConfig -> {
                    headerConfig.frameOptions(
                        frameOptionsConfig -> frameOptionsConfig.disable()
                    );
                }
            )
            .authorizeHttpRequests(
                authroizeRequest -> {
                    authroizeRequest
                        .requestMatchers("/users").permitAll() // 회원가입
                        .anyRequest().authenticated();
                }
            )
            .userDetailsService(customUserDetailsService)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
