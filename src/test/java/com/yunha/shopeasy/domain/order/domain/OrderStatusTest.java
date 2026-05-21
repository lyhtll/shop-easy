package com.yunha.shopeasy.domain.order.domain;

import com.yunha.shopeasy.global.error.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 상태 전이 검증")
class OrderStatusTest {

    @Test
    @DisplayName("PENDING → PAID 전이 성공")
    void pending_to_paid() {
        assertThatCode(() -> OrderStatus.PENDING.validateTransition(OrderStatus.PAID))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("PENDING → CANCELLED 전이 성공")
    void pending_to_cancelled() {
        assertThatCode(() -> OrderStatus.PENDING.validateTransition(OrderStatus.CANCELLED))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("PENDING → REFUNDED 전이 불가")
    void pending_to_refunded_fails() {
        assertThatThrownBy(() -> OrderStatus.PENDING.validateTransition(OrderStatus.REFUNDED))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("PAID → REFUNDED 전이 성공")
    void paid_to_refunded() {
        assertThatCode(() -> OrderStatus.PAID.validateTransition(OrderStatus.REFUNDED))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("PAID → CANCELLED 전이 불가")
    void paid_to_cancelled_fails() {
        assertThatThrownBy(() -> OrderStatus.PAID.validateTransition(OrderStatus.CANCELLED))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("CANCELLED 상태에서 모든 전이 불가")
    void cancelled_to_any_fails(OrderStatus next) {
        assertThatThrownBy(() -> OrderStatus.CANCELLED.validateTransition(next))
                .isInstanceOf(CustomException.class);
    }

    @ParameterizedTest
    @EnumSource(OrderStatus.class)
    @DisplayName("REFUNDED 상태에서 모든 전이 불가")
    void refunded_to_any_fails(OrderStatus next) {
        assertThatThrownBy(() -> OrderStatus.REFUNDED.validateTransition(next))
                .isInstanceOf(CustomException.class);
    }
}
