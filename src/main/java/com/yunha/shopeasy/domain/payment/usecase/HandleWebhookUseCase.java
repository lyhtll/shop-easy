package com.yunha.shopeasy.domain.payment.usecase;

import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.service.OrderService;
import com.yunha.shopeasy.domain.payment.domain.Payment;
import com.yunha.shopeasy.domain.payment.domain.PaymentStatus;
import com.yunha.shopeasy.domain.payment.dto.request.TossWebhookRequest;
import com.yunha.shopeasy.domain.payment.error.PaymentError;
import com.yunha.shopeasy.domain.payment.repository.PaymentRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class HandleWebhookUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Transactional
    public void execute(TossWebhookRequest request) {
        paymentRepository.findByOrderId(Long.parseLong(request.orderId()))
                .ifPresent(payment -> syncStatus(payment, request.status()));
    }

    private void syncStatus(Payment payment, String tossStatus) {
        switch (tossStatus) {
            case "DONE" -> {
                if (payment.getStatus() != PaymentStatus.PAID) {
                    payment.updateStatus(PaymentStatus.PAID);
                    orderService.updateStatus(payment.getOrderId(), OrderStatus.PAID);
                }
            }
            case "CANCELED" -> {
                if (payment.getStatus() != PaymentStatus.CANCELLED) {
                    payment.updateStatus(PaymentStatus.CANCELLED);
                    orderService.updateStatus(payment.getOrderId(), OrderStatus.REFUNDED);
                }
            }
            default -> payment.updateStatus(PaymentStatus.FAILED);
        }
    }
}
