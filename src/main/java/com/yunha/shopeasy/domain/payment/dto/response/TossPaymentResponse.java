package com.yunha.shopeasy.domain.payment.dto.response;

import java.math.BigDecimal;

public record TossPaymentResponse(
        String paymentKey,
        String orderId,
        String status,
        BigDecimal totalAmount
) {}
