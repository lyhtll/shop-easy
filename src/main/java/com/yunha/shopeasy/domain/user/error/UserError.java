package com.yunha.shopeasy.domain.user.error;

import com.yunha.shopeasy.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserError implements CustomError {
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("U002", "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD("U003", "비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS("U004", "이메일 또는 비밀번호가 올바르지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
