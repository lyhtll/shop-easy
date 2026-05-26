package com.yunha.shopeasy.domain.auth.usecase;

import com.yunha.shopeasy.domain.auth.domain.RefreshToken;
import com.yunha.shopeasy.domain.auth.dto.request.ReissueRequest;
import com.yunha.shopeasy.domain.auth.dto.response.LoginResponse;
import com.yunha.shopeasy.domain.auth.error.AuthError;
import com.yunha.shopeasy.domain.auth.repository.RefreshTokenRepository;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.error.UserError;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.global.error.CustomException;
import com.yunha.shopeasy.global.security.jwt.provider.JwtProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReissueUseCase {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public LoginResponse execute(ReissueRequest request) {
        String token = request.refreshToken();
        Long userId;
        try {
            userId = jwtProvider.getUserId(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(AuthError.INVALID_REFRESH_TOKEN);
        }
        RefreshToken stored = refreshTokenRepository.findById(userId.toString())
                .orElseThrow(() -> new CustomException(AuthError.REFRESH_TOKEN_NOT_FOUND));
        if (!stored.getToken().equals(token)) {
            throw new CustomException(AuthError.INVALID_REFRESH_TOKEN);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
        refreshTokenRepository.deleteById(userId.toString());
        String newAccess = jwtProvider.generateAccessToken(user.getId(), user.getRole());
        String newRefresh = jwtProvider.generateRefreshToken(user.getId());
        refreshTokenRepository.save(new RefreshToken(user.getId().toString(), newRefresh));
        return new LoginResponse(newAccess, newRefresh);
    }
}
