package com.yunha.shopeasy.domain.user.usecase;

import com.yunha.shopeasy.domain.user.dto.request.UpdateUserRequest;
import com.yunha.shopeasy.domain.user.error.UserError;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UpdateMeUseCase {

    private final UserRepository userRepository;

    @Transactional
    public void execute(Long userId, UpdateUserRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND))
                .updateName(request.name());
    }
}