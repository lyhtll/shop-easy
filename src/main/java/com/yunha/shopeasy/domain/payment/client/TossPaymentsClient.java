package com.yunha.shopeasy.domain.payment.client;

import com.yunha.shopeasy.domain.payment.dto.request.TossPaymentConfirmRequest;
import com.yunha.shopeasy.domain.payment.dto.response.TossPaymentResponse;
import com.yunha.shopeasy.domain.payment.error.PaymentError;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentsClient {

    private final RestClient tossPaymentsRestClient;

    public TossPaymentResponse confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
        try {
            return tossPaymentsRestClient.post()
                    .uri("/payments/confirm")
                    .body(new TossPaymentConfirmRequest(paymentKey, orderId, amount))
                    .retrieve()
                    .body(TossPaymentResponse.class);
        } catch (Exception e) {
            log.error("Toss API error", e);
            throw new CustomException(PaymentError.TOSS_API_CALL_FAILED);
        }
    }

    public void cancelPayment(String paymentKey, String cancelReason) {
        try {
            tossPaymentsRestClient.post()
                    .uri("/payments/{paymentKey}/cancel", paymentKey)
                    .body(Map.of("cancelReason", cancelReason))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Toss API error", e);
            throw new CustomException(PaymentError.TOSS_API_CALL_FAILED);
        }
    }
}
