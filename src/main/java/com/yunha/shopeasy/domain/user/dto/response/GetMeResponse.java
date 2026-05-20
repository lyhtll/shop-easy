package com.yunha.shopeasy.domain.user.dto.response;

import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;

public record GetMeResponse(Long id, String email, String name, UserRole role) {

    public static GetMeResponse from(User user) {
        return new GetMeResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}