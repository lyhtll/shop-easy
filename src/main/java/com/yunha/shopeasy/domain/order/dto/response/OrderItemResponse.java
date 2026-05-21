package com.yunha.shopeasy.domain.order.dto.response;

import com.yunha.shopeasy.domain.order.domain.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(Long productId, int quantity, BigDecimal price) {

    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(item.getProductId(), item.getQuantity(), item.getPrice());
    }
}