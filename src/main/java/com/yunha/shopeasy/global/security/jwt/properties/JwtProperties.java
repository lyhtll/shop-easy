package com.yunha.shopeasy.global.security.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long accessExpiration, long refreshExpiration) {}
