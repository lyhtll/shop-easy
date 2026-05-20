package com.yunha.shopeasy.domain.auth.usecase;

import com.yunha.shopeasy.domain.auth.domain.RefreshToken;
import com.yunha.shopeasy.domain.auth.dto.request.LoginRequest;
import com.yunha.shopeasy.domain.auth.dto.response.LoginResponse;
import com.yunha.shopeasy.domain.auth.repository.RefreshTokenRepository;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.error.UserError;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.global.error.CustomException;
import com.yunha.shopeasy.global.security.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public LoginResponse execute(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(UserError.INVALID_CREDENTIALS));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(UserError.INVALID_CREDENTIALS);
        }
        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        refreshTokenRepository.save(new RefreshToken(user.getId(), refreshToken));
        return new LoginResponse(accessToken, refreshToken);
    }
}
