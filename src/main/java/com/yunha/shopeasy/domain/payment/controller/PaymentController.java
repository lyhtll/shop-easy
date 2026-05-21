package com.yunha.shopeasy.domain.payment.controller;

import com.yunha.shopeasy.domain.payment.dto.request.PaymentCancelRequest;
import com.yunha.shopeasy.domain.payment.dto.request.PaymentConfirmRequest;
import com.yunha.shopeasy.domain.payment.dto.request.TossWebhookRequest;
import com.yunha.shopeasy.domain.payment.dto.response.PaymentResponse;
import com.yunha.shopeasy.domain.payment.usecase.CancelPaymentUseCase;
import com.yunha.shopeasy.domain.payment.usecase.ConfirmPaymentUseCase;
import com.yunha.shopeasy.domain.payment.usecase.HandleWebhookUseCase;
import com.yunha.shopeasy.global.security.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ConfirmPaymentUseCase confirmPaymentUseCase;
    private final CancelPaymentUseCase cancelPaymentUseCase;
    private final HandleWebhookUseCase handleWebhookUseCase;

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirm(@Valid @RequestBody PaymentConfirmRequest request) {
        return ResponseEntity.ok(confirmPaymentUseCase.execute(SecurityUtil.getCurrentUserId(), request));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id, @Valid @RequestBody PaymentCancelRequest request) {
        cancelPaymentUseCase.execute(SecurityUtil.getCurrentUserId(), id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> webhook(@RequestBody TossWebhookRequest request) {
        handleWebhookUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}
