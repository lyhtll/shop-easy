package com.yunha.shopeasy.domain.payment.dto.request;

import java.math.BigDecimal;

public record TossPaymentConfirmRequest(
        String paymentKey,
        String orderId,
        BigDecimal amount
) {}
