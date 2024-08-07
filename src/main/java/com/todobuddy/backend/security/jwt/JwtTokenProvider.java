package com.todobuddy.backend.security.jwt;

import com.todobuddy.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

    private final String secretKey;
    private final long expiredTime;
    private final String issuer;
    private final Key key;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.expiration-time}") long expiredTime,
        @Value("${jwt.issuer}") String issuer) {

        this.secretKey = secretKey;
        this.expiredTime = expiredTime;
        this.issuer = issuer;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public String generateToken(User user) {
        long now = new Date().getTime();

        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("auth", user.getRole())
            .setIssuer(issuer)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + expiredTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // JWT 토큰 검증
    public Claims validateToken(String jwtToken) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
    }

}
