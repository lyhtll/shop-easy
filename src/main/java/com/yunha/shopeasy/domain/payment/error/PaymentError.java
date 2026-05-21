package com.yunha.shopeasy.domain.payment.error;

import com.yunha.shopeasy.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentError implements CustomError {
    PAYMENT_NOT_FOUND("PAY001", "결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AMOUNT_MISMATCH("PAY002", "결제 금액이 주문 금액과 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    TOSS_API_CALL_FAILED("PAY003", "결제 처리 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ALREADY_PAID("PAY004", "이미 결제된 주문입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}