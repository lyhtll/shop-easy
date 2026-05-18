package com.yunha.shopeasy.global.security.jwt.config;

import com.yunha.shopeasy.global.security.jwt.properties.JwtProperties;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {

    @Bean
    public SecretKey signingKey(JwtProperties jwtProperties) {
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
