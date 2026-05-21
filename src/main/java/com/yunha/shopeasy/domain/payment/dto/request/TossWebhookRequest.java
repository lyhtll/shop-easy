package com.yunha.shopeasy.domain.payment.dto.request;

import java.math.BigDecimal;

public record TossWebhookRequest(
        String paymentKey,
        String orderId,
        String status,
        BigDecimal totalAmount
) {}
