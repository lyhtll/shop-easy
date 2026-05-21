package com.yunha.shopeasy.domain.order.usecase;

import com.yunha.shopeasy.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderService orderService;

    public void execute(Long userId, Long orderId) {
        orderService.cancel(userId, orderId);
    }
}