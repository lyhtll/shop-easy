package com.yunha.shopeasy.domain.order.usecase;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderItem;
import com.yunha.shopeasy.domain.order.dto.request.CreateOrderItemRequest;
import com.yunha.shopeasy.domain.order.dto.request.CreateOrderRequest;
import com.yunha.shopeasy.domain.order.repository.OrderRepository;
import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Transactional
    public Long execute(Long userId, CreateOrderRequest request) {
        Order order = Order.builder()
                .userId(userId)
                .totalPrice(BigDecimal.ZERO)
                .build();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CreateOrderItemRequest itemReq : request.items()) {
            Product product = productService.findById(itemReq.productId());
            product.decreaseStock(itemReq.quantity());

            totalPrice = totalPrice.add(
                    product.getPrice().multiply(BigDecimal.valueOf(itemReq.quantity()))
            );

            order.getOrderItems().add(OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .quantity(itemReq.quantity())
                    .price(product.getPrice())
                    .build());
        }

        order.updateTotalPrice(totalPrice);
        return orderRepository.save(order).getId();
    }
}