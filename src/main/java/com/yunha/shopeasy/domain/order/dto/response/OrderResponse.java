package com.yunha.shopeasy.domain.order.dto.response;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(Long id, BigDecimal totalPrice, OrderStatus status, LocalDateTime createdAt) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getTotalPrice(), order.getStatus(), order.getCreatedAt());
    }
}