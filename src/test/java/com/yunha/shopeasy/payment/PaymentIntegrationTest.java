package com.yunha.shopeasy.payment;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.repository.OrderRepository;
import com.yunha.shopeasy.domain.payment.client.TossPaymentsClient;
import com.yunha.shopeasy.domain.payment.dto.request.PaymentConfirmRequest;
import com.yunha.shopeasy.domain.payment.dto.response.TossPaymentResponse;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("결제 통합 테스트")
class PaymentIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private OrderRepository orderRepository;

    @MockitoBean
    private TossPaymentsClient tossPaymentsClient;

    private Long userId;
    private Long otherUserId;
    private Order order;

    @BeforeEach
    void setUpData() {
        userId = userRepository.save(User.builder()
                .email("payer@test.com")
                .password(passwordEncoder.encode("pw123456"))
                .name("결제유저")
                .role(UserRole.USER)
                .build()).getId();

        otherUserId = userRepository.save(User.builder()
                .email("other@test.com")
                .password(passwordEncoder.encode("pw123456"))
                .name("타인유저")
                .role(UserRole.USER)
                .build()).getId();

        order = orderRepository.save(Order.builder()
                .userId(userId)
                .totalPrice(new BigDecimal("50000"))
                .build());

        when(tossPaymentsClient.confirmPayment(any(), any(), any()))
                .thenReturn(new TossPaymentResponse(
                        "pk_test_123", order.getId().toString(), "DONE", new BigDecimal("50000")));
    }

    @Test
    @DisplayName("결제 승인 성공 — 200 및 주문 상태 PAID 전환")
    void confirmPayment_success() throws Exception {
        PaymentConfirmRequest request = new PaymentConfirmRequest(
                "pk_test_123", order.getId().toString(), new BigDecimal("50000"), "idem-key-001");

        mockMvc.perform(post("/api/payments/confirm")
                        .header("Authorization", userToken(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentKey").value("pk_test_123"))
                .andExpect(jsonPath("$.status").value("PAID"));

        Order updated = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("결제 승인 실패 — 금액 불일치 400, Toss API 호출 안 됨")
    void confirmPayment_amountMismatch() throws Exception {
        PaymentConfirmRequest request = new PaymentConfirmRequest(
                "pk_test_123", order.getId().toString(), new BigDecimal("1"), "idem-key-002");

        mockMvc.perform(post("/api/payments/confirm")
                        .header("Authorization", userToken(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("PAY002"));

        verify(tossPaymentsClient, never()).confirmPayment(any(), any(), any());
    }

    @Test
    @DisplayName("타인 주문 결제 시도 403")
    void confirmPayment_otherUserOrder() throws Exception {
        PaymentConfirmRequest request = new PaymentConfirmRequest(
                "pk_test_123", order.getId().toString(), new BigDecimal("50000"), "idem-key-003");

        mockMvc.perform(post("/api/payments/confirm")
                        .header("Authorization", userToken(otherUserId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("O002"));
    }

    @Test
    @DisplayName("결제 승인 — 인증 없이 접근 401")
    void confirmPayment_unauthorized() throws Exception {
        PaymentConfirmRequest request = new PaymentConfirmRequest(
                "pk_test_123", order.getId().toString(), new BigDecimal("50000"), "idem-key-004");

        mockMvc.perform(post("/api/payments/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
