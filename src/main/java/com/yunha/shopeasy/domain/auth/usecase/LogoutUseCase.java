package com.yunha.shopeasy.domain.auth.usecase;

import com.yunha.shopeasy.domain.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(Long userId) {
        refreshTokenRepository.deleteById(userId.toString());
    }
}