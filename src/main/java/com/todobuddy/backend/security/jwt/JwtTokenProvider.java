package com.todobuddy.backend.security.jwt;

import com.todobuddy.backend.dto.AuthResponse;
import com.todobuddy.backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

    private final String secretKey;
    private final long accessExpiredTime;
    private final long refreshExpiredTime;
    private final String issuer;
    private final Key key;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token-expiration-time}") long accessExpiredTime,
        @Value("${jwt.refresh-token-expiration-time}") long refreshExpiredTime,
        @Value("${jwt.issuer}") String issuer) {

        this.secretKey = secretKey;
        this.accessExpiredTime = accessExpiredTime;
        this.refreshExpiredTime = refreshExpiredTime;
        this.issuer = issuer;

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    // JWT 토큰 생성
    public AuthResponse generateToken(User user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);
        return new AuthResponse(accessToken, refreshToken, getExpirationDateFromToken(refreshToken), "Bearer");
    }

    // ACCESS TOKEN 생성
    public String generateAccessToken(User user) {
        long now = new Date().getTime();
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("auth", user.getRole())
            .setIssuer(issuer)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + accessExpiredTime))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    // REFRESH TOKEN 생성
    private String generateRefreshToken(User user) {
        long now = new Date().getTime();
        return Jwts.builder()
            .setSubject(user.getEmail())
            .claim("auth", user.getRole())
            .setIssuer(issuer)
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + refreshExpiredTime))
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

    public String getExpirationDateFromToken(String token)  {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(claims.getExpiration());
    }

}
