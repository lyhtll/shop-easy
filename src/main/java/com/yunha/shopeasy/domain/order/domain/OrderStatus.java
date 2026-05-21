package com.yunha.shopeasy.domain.order.domain;

import com.yunha.shopeasy.domain.order.error.OrderError;
import com.yunha.shopeasy.global.error.CustomException;

public enum OrderStatus {
    PENDING, PAID, CANCELLED, REFUNDED;

    public void validateTransition(OrderStatus next) {
        boolean valid = switch (this) {
            case PENDING -> next == PAID || next == CANCELLED;
            case PAID -> next == REFUNDED;
            case CANCELLED, REFUNDED -> false;
        };
        if (!valid) {
            throw new CustomException(OrderError.INVALID_STATUS_TRANSITION);
        }
    }
}