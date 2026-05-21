package com.yunha.shopeasy.domain.product.domain;

import com.yunha.shopeasy.global.error.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 도메인 검증")
class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("테스트 상품")
                .price(new BigDecimal("10000"))
                .stock(10)
                .category(ProductCategory.ELECTRONICS)
                .description("테스트용 상품입니다.")
                .build();
    }

    @Test
    @DisplayName("재고 감소 성공")
    void decreaseStock_success() {
        product.decreaseStock(3);

        assertThat(product.getStock()).isEqualTo(7);
    }

    @Test
    @DisplayName("재고 전량 차감 성공 — 경계값")
    void decreaseStock_exact_amount() {
        assertThatCode(() -> product.decreaseStock(10))
                .doesNotThrowAnyException();

        assertThat(product.getStock()).isZero();
    }

    @Test
    @DisplayName("재고 초과 차감 시 OUT_OF_STOCK 예외")
    void decreaseStock_out_of_stock() {
        assertThatThrownBy(() -> product.decreaseStock(11))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("상품 정보 수정")
    void update_product() {
        product.update("수정된 상품", new BigDecimal("20000"), 5, ProductCategory.BOOKS, "수정됨");

        assertThat(product.getName()).isEqualTo("수정된 상품");
        assertThat(product.getPrice()).isEqualByComparingTo("20000");
        assertThat(product.getStock()).isEqualTo(5);
        assertThat(product.getCategory()).isEqualTo(ProductCategory.BOOKS);
    }
}
