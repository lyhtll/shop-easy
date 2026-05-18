package com.yunha.shopeasy.global.security.jwt.provider;

import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public record JwtClaims(Long userId, UserRole role) {}

    public String generateAccessToken(Long userId, UserRole role) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.accessExpiration()))
                .signWith(signingKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "refresh")
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.refreshExpiration()))
                .signWith(signingKey)
                .compact();
    }

    public Long getUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    public JwtClaims extractClaims(String token) {
        Claims claims = parseClaims(token);
        return new JwtClaims(
                Long.parseLong(claims.getSubject()),
                UserRole.valueOf(claims.get("role", String.class))
        );
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
