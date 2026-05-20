package com.yunha.shopeasy.domain.user.usecase;

import com.yunha.shopeasy.domain.user.dto.response.GetMeResponse;
import com.yunha.shopeasy.domain.user.error.UserError;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMeUseCase {

    private final UserRepository userRepository;

    public GetMeResponse execute(Long userId) {
        return userRepository.findById(userId)
                .map(GetMeResponse::from)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));
    }
}