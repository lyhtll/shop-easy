package com.yunha.shopeasy.global.security.util;

import com.yunha.shopeasy.domain.auth.error.AuthError;
import com.yunha.shopeasy.global.error.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof Long)) {
            throw new CustomException(AuthError.UNAUTHORIZED);
        }
        return (Long) auth.getPrincipal();
    }
}