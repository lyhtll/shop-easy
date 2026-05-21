package com.yunha.shopeasy.domain.payment.usecase;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.error.OrderError;
import com.yunha.shopeasy.domain.order.service.OrderService;
import com.yunha.shopeasy.domain.payment.client.TossPaymentsClient;
import com.yunha.shopeasy.domain.payment.domain.Payment;
import com.yunha.shopeasy.domain.payment.domain.PaymentStatus;
import com.yunha.shopeasy.domain.payment.dto.request.PaymentCancelRequest;
import com.yunha.shopeasy.domain.payment.error.PaymentError;
import com.yunha.shopeasy.domain.payment.repository.PaymentRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CancelPaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final TossPaymentsClient tossPaymentsClient;

    @Transactional
    public void execute(Long userId, Long paymentId, PaymentCancelRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CustomException(PaymentError.PAYMENT_NOT_FOUND));

        Order order = orderService.findById(payment.getOrderId());
        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderError.ORDER_ACCESS_DENIED);
        }

        tossPaymentsClient.cancelPayment(payment.getPaymentKey(), request.cancelReason());
        payment.updateStatus(PaymentStatus.CANCELLED);
        orderService.updateStatus(order.getId(), OrderStatus.REFUNDED);
    }
}
