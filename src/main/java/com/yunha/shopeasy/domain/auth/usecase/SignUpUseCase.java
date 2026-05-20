package com.yunha.shopeasy.domain.auth.usecase;

import com.yunha.shopeasy.domain.auth.dto.request.SignUpRequest;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.domain.user.error.UserError;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignUpUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new CustomException(UserError.EMAIL_ALREADY_EXISTS);
        }
        userRepository.save(User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .role(UserRole.USER)
                .build());
    }
}
