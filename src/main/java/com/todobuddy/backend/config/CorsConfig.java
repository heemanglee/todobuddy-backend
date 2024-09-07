package com.todobuddy.backend.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${server.domain}")
    private String doamin; // static 변수에 @Value를 사용하면 null이 저장된다.
    @Value("${front.domain}")
    private String frontDomain;

    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();

        // allowedOriginPatterns : 리소스를 허용할 URL
        List<String> allowedOriginPatterns = getAllowedOriginPatterns();

        // allowedHttpMethods : 허용할 HTTP 메서드
        List<String> allowedHttpMethods = List.of("GET", "POST", "PUT", "DELETE", "PATCH",
            "OPTIONS");

        configuration.setAllowedOrigins(allowedOriginPatterns);
        configuration.setAllowedMethods(allowedHttpMethods);
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    private List<String> getAllowedOriginPatterns() {
        List<String> allowedOriginPatterns = new ArrayList<>();
        allowedOriginPatterns.add("http://localhost:3000");
        allowedOriginPatterns.add("http://127.0.0.1:3000");
        allowedOriginPatterns.add(doamin);
        allowedOriginPatterns.add(frontDomain);
        return allowedOriginPatterns;
    }
}
