package com.yunha.shopeasy.domain.payment.usecase;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.error.OrderError;
import com.yunha.shopeasy.domain.order.service.OrderService;
import com.yunha.shopeasy.domain.payment.client.TossPaymentsClient;
import com.yunha.shopeasy.domain.payment.domain.Payment;
import com.yunha.shopeasy.domain.payment.domain.PaymentStatus;
import com.yunha.shopeasy.domain.payment.dto.request.PaymentConfirmRequest;
import com.yunha.shopeasy.domain.payment.dto.response.PaymentResponse;
import com.yunha.shopeasy.domain.payment.error.PaymentError;
import com.yunha.shopeasy.domain.payment.repository.PaymentRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ConfirmPaymentUseCase {

    private final OrderService orderService;
    private final PaymentRepository paymentRepository;
    private final TossPaymentsClient tossPaymentsClient;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String IDEMPOTENCY_PREFIX = "idempotency:";

    @Transactional
    public PaymentResponse execute(Long userId, PaymentConfirmRequest request) {
        // ① 중복 요청 체크
        String idempotencyKey = IDEMPOTENCY_PREFIX + request.idempotencyKey();
        String cached = redisTemplate.opsForValue().get(idempotencyKey);
        if (cached != null) {
            return paymentRepository.findByOrderId(Long.parseLong(cached))
                    .map(PaymentResponse::from)
                    .orElseThrow(() -> new CustomException(PaymentError.PAYMENT_NOT_FOUND));
        }

        // ② 주문 조회 및 소유자 검증 (orderId 형식: "order-000001" 또는 "order-000001-{timestamp}" 또는 숫자 문자열)
        String rawId = request.orderId().startsWith("order-")
                ? request.orderId().substring(6).split("-")[0]
                : request.orderId().split("-")[0];
        Order order = orderService.findById(Long.parseLong(rawId));
        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderError.ORDER_ACCESS_DENIED);
        }

        // ③ 금액 위변조 검증 (결제 승인 전)
        if (order.getTotalPrice().compareTo(request.amount()) != 0) {
            throw new CustomException(PaymentError.AMOUNT_MISMATCH);
        }

        // ④ 토스페이먼츠 결제 승인 API 호출
        tossPaymentsClient.confirmPayment(request.paymentKey(), request.orderId(), request.amount());

        // ⑤ 결제 저장 + 주문 상태 변경 (하나의 트랜잭션)
        Payment payment = paymentRepository.save(Payment.builder()
                .orderId(order.getId())
                .paymentKey(request.paymentKey())
                .amount(request.amount())
                .status(PaymentStatus.PAID)
                .build());
        orderService.updateStatus(order.getId(), OrderStatus.PAID);

        // ⑥ 커밋 성공 이후에만 Redis에 저장 (롤백 시 Redis 불일치 방지)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisTemplate.opsForValue().set(idempotencyKey, order.getId().toString(), Duration.ofHours(24));
            }
        });

        return PaymentResponse.from(payment);
    }
}
