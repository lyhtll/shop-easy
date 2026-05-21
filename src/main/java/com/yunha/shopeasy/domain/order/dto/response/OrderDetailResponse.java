package com.yunha.shopeasy.domain.order.dto.response;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        BigDecimal totalPrice,
        OrderStatus status,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getOrderItems().stream().map(OrderItemResponse::from).toList(),
                order.getCreatedAt()
        );
    }
}