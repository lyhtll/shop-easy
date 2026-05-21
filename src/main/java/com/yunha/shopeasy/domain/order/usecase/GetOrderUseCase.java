package com.yunha.shopeasy.domain.order.usecase;

import com.yunha.shopeasy.domain.order.dto.response.OrderDetailResponse;
import com.yunha.shopeasy.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetOrderUseCase {

    private final OrderService orderService;

    public OrderDetailResponse execute(Long userId, Long orderId) {
        return orderService.getOrderDetail(userId, orderId);
    }
}