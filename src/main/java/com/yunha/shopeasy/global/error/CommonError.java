package com.yunha.shopeasy.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonError implements CustomError {
    VALIDATION_ERROR("C001", "입력값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("C002", "요청 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("C003", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_ERROR("S001", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
