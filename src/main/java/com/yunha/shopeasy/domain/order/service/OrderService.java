package com.yunha.shopeasy.domain.order.service;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.dto.response.OrderDetailResponse;
import com.yunha.shopeasy.domain.order.error.OrderError;
import com.yunha.shopeasy.domain.order.repository.OrderRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
        Order order = findById(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderError.ORDER_ACCESS_DENIED);
        }
        return OrderDetailResponse.from(order);
    }

    @Transactional
    public void cancel(Long userId, Long orderId) {
        Order order = findById(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new CustomException(OrderError.ORDER_ACCESS_DENIED);
        }
        order.changeStatus(OrderStatus.CANCELLED);
    }

    @Transactional
    public void updateStatus(Long orderId, OrderStatus status) {
        findById(orderId).changeStatus(status);
    }

    public Order findById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(OrderError.ORDER_NOT_FOUND));
    }
}