package com.yunha.shopeasy.domain.payment.dto.response;

import com.yunha.shopeasy.domain.payment.domain.Payment;
import com.yunha.shopeasy.domain.payment.domain.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(Long id, Long orderId, String paymentKey, BigDecimal amount, PaymentStatus status) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getPaymentKey(),
                payment.getAmount(),
                payment.getStatus()
        );
    }
}
