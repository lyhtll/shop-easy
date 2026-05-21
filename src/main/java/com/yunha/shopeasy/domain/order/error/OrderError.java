package com.yunha.shopeasy.domain.order.error;

import com.yunha.shopeasy.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderError implements CustomError {
    ORDER_NOT_FOUND("O001", "주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_ACCESS_DENIED("O002", "본인의 주문만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),
    INVALID_STATUS_TRANSITION("O003", "허용되지 않은 주문 상태 변경입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}