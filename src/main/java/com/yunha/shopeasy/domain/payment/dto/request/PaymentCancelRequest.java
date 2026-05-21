package com.yunha.shopeasy.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PaymentCancelRequest(
        @NotBlank String cancelReason
) {}
