package com.yunha.shopeasy.domain.order.controller;

import com.yunha.shopeasy.domain.order.dto.request.CreateOrderRequest;
import com.yunha.shopeasy.domain.order.dto.response.OrderDetailResponse;
import com.yunha.shopeasy.domain.order.dto.response.OrderResponse;
import com.yunha.shopeasy.domain.order.usecase.CancelOrderUseCase;
import com.yunha.shopeasy.domain.order.usecase.CreateOrderUseCase;
import com.yunha.shopeasy.domain.order.usecase.GetMyOrdersUseCase;
import com.yunha.shopeasy.domain.order.usecase.GetOrderUseCase;
import com.yunha.shopeasy.global.security.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetMyOrdersUseCase getMyOrdersUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    @PostMapping
    public ResponseEntity<Long> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createOrderUseCase.execute(SecurityUtil.getCurrentUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(getMyOrdersUseCase.execute(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(getOrderUseCase.execute(SecurityUtil.getCurrentUserId(), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        cancelOrderUseCase.execute(SecurityUtil.getCurrentUserId(), id);
        return ResponseEntity.noContent().build();
    }
}