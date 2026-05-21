package com.yunha.shopeasy.order;

import com.yunha.shopeasy.domain.order.domain.Order;
import com.yunha.shopeasy.domain.order.domain.OrderStatus;
import com.yunha.shopeasy.domain.order.dto.request.CreateOrderItemRequest;
import com.yunha.shopeasy.domain.order.dto.request.CreateOrderRequest;
import com.yunha.shopeasy.domain.order.repository.OrderRepository;
import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.domain.user.domain.User;
import com.yunha.shopeasy.domain.user.domain.UserRole;
import com.yunha.shopeasy.domain.user.repository.UserRepository;
import com.yunha.shopeasy.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("주문 통합 테스트")
class OrderIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Long userAId;
    private Long userBId;
    private Product product;

    @BeforeEach
    void setUpData() {
        userAId = userRepository.save(User.builder()
                .email("userA@test.com")
                .password(passwordEncoder.encode("pw123456"))
                .name("유저A")
                .role(UserRole.USER)
                .build()).getId();

        userBId = userRepository.save(User.builder()
                .email("userB@test.com")
                .password(passwordEncoder.encode("pw123456"))
                .name("유저B")
                .role(UserRole.USER)
                .build()).getId();

        product = productRepository.save(Product.builder()
                .name("테스트 상품")
                .price(new BigDecimal("10000"))
                .stock(5)
                .category(ProductCategory.ELECTRONICS)
                .build());
    }

    @Test
    @DisplayName("주문 생성 성공 — 201 및 재고 감소")
    void createOrder_success() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(product.getId(), 2)));

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", userToken(userAId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        Product updated = productRepository.findById(product.getId()).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(updated.getStock()).isEqualTo(3);
    }

    @Test
    @DisplayName("주문 생성 실패 — 재고 부족 409")
    void createOrder_outOfStock() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(product.getId(), 10)));

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", userToken(userAId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("P002"));
    }

    @Test
    @DisplayName("주문 생성 실패 — 존재하지 않는 상품 404")
    void createOrder_productNotFound() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest(
                List.of(new CreateOrderItemRequest(999999L, 1)));

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", userToken(userAId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"));
    }

    @Test
    @DisplayName("본인 주문 상세 조회 200")
    void getOrder_success() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .userId(userAId)
                .totalPrice(new BigDecimal("10000"))
                .build());

        mockMvc.perform(get("/api/orders/{id}", order.getId())
                        .header("Authorization", userToken(userAId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("타인 주문 조회 시 403")
    void getOrder_accessDenied() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .userId(userAId)
                .totalPrice(new BigDecimal("10000"))
                .build());

        mockMvc.perform(get("/api/orders/{id}", order.getId())
                        .header("Authorization", userToken(userBId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("O002"));
    }

    @Test
    @DisplayName("본인 주문 취소 204")
    void cancelOrder_success() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .userId(userAId)
                .totalPrice(new BigDecimal("10000"))
                .build());

        mockMvc.perform(delete("/api/orders/{id}", order.getId())
                        .header("Authorization", userToken(userAId)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("타인 주문 취소 시도 403")
    void cancelOrder_accessDenied() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .userId(userAId)
                .totalPrice(new BigDecimal("10000"))
                .build());

        mockMvc.perform(delete("/api/orders/{id}", order.getId())
                        .header("Authorization", userToken(userBId)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("O002"));
    }

    @Test
    @DisplayName("이미 취소된 주문 재취소 시 400")
    void cancelOrder_alreadyCancelled() throws Exception {
        Order order = orderRepository.save(Order.builder()
                .userId(userAId)
                .totalPrice(new BigDecimal("10000"))
                .build());
        order.changeStatus(OrderStatus.CANCELLED);

        mockMvc.perform(delete("/api/orders/{id}", order.getId())
                        .header("Authorization", userToken(userAId)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("O003"));
    }

    @Test
    @DisplayName("내 주문 목록 조회 200")
    void getMyOrders_success() throws Exception {
        orderRepository.save(Order.builder().userId(userAId).totalPrice(new BigDecimal("10000")).build());
        orderRepository.save(Order.builder().userId(userAId).totalPrice(new BigDecimal("20000")).build());
        orderRepository.save(Order.builder().userId(userBId).totalPrice(new BigDecimal("5000")).build());

        mockMvc.perform(get("/api/orders")
                        .header("Authorization", userToken(userAId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
