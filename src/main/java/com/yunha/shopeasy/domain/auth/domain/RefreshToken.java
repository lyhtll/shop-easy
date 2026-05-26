package com.yunha.shopeasy.domain.auth.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refresh", timeToLive = 604800)
@Getter
public class RefreshToken {

    @Id
    private String userId;

    private String token;

    public RefreshToken(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}