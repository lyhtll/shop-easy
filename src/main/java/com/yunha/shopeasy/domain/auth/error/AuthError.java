package com.yunha.shopeasy.domain.auth.error;

import com.yunha.shopeasy.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthError implements CustomError {
    INVALID_TOKEN("A001", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("A002", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND("A003", "리프레시 토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("A004", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED("A005", "인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED("A006", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}