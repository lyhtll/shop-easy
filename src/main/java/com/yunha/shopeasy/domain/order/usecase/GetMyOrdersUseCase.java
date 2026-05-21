package com.yunha.shopeasy.domain.order.usecase;

import com.yunha.shopeasy.domain.order.dto.response.OrderResponse;
import com.yunha.shopeasy.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMyOrdersUseCase {

    private final OrderRepository orderRepository;

    public List<OrderResponse> execute(Long userId) {
        return orderRepository.findAllByUserId(userId).stream()
                .map(OrderResponse::from)
                .toList();
    }
}