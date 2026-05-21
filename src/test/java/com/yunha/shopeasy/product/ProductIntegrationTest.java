package com.yunha.shopeasy.product;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import com.yunha.shopeasy.domain.product.dto.request.CreateProductRequest;
import com.yunha.shopeasy.domain.product.dto.request.UpdateProductRequest;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.support.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("상품 통합 테스트")
class ProductIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    private Product savedProduct() {
        return productRepository.save(Product.builder()
                .name("노트북")
                .price(new BigDecimal("1500000"))
                .stock(10)
                .category(ProductCategory.ELECTRONICS)
                .description("고성능 노트북")
                .build());
    }

    @Test
    @DisplayName("ADMIN — 상품 생성 201")
    void create_product_admin() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "스마트폰", new BigDecimal("900000"), 50,
                ProductCategory.ELECTRONICS, "최신형 스마트폰");

        mockMvc.perform(post("/api/products")
                        .header("Authorization", adminToken(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().string(org.hamcrest.Matchers.matchesPattern("\\d+")));
    }

    @Test
    @DisplayName("USER — 상품 생성 시도 403")
    void create_product_user_forbidden() throws Exception {
        CreateProductRequest request = new CreateProductRequest(
                "상품", new BigDecimal("10000"), 5,
                ProductCategory.BOOKS, null);

        mockMvc.perform(post("/api/products")
                        .header("Authorization", userToken(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("상품 단건 조회 200")
    void get_product() throws Exception {
        Product product = savedProduct();

        mockMvc.perform(get("/api/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("노트북"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.category").value("ELECTRONICS"));
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 404")
    void get_product_not_found() throws Exception {
        mockMvc.perform(get("/api/products/{id}", 999999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("P001"));
    }

    @Test
    @DisplayName("상품 목록 조회 — 전체 200")
    void search_products_all() throws Exception {
        savedProduct();

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("상품 목록 조회 — 키워드 필터링")
    void search_products_keyword() throws Exception {
        savedProduct();
        productRepository.save(Product.builder()
                .name("마우스")
                .price(new BigDecimal("50000"))
                .stock(100)
                .category(ProductCategory.ELECTRONICS)
                .build());

        mockMvc.perform(get("/api/products").param("keyword", "노트"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("노트북"));
    }

    @Test
    @DisplayName("상품 목록 조회 — 카테고리 필터링")
    void search_products_category() throws Exception {
        savedProduct();
        productRepository.save(Product.builder()
                .name("소설책")
                .price(new BigDecimal("15000"))
                .stock(30)
                .category(ProductCategory.BOOKS)
                .build());

        mockMvc.perform(get("/api/products").param("category", "BOOKS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("소설책"));
    }

    @Test
    @DisplayName("상품 수정 204")
    void update_product() throws Exception {
        Product product = savedProduct();
        UpdateProductRequest request = new UpdateProductRequest(
                "노트북 프로", new BigDecimal("2000000"), 5,
                ProductCategory.ELECTRONICS, "업그레이드 버전");

        mockMvc.perform(put("/api/products/{id}", product.getId())
                        .header("Authorization", adminToken(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 삭제 204")
    void delete_product() throws Exception {
        Product product = savedProduct();

        mockMvc.perform(delete("/api/products/{id}", product.getId())
                        .header("Authorization", adminToken(1L)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 가격 범위 필터링")
    void search_products_price_range() throws Exception {
        savedProduct(); // 1,500,000
        productRepository.save(Product.builder()
                .name("저렴한 상품")
                .price(new BigDecimal("10000"))
                .stock(5)
                .category(ProductCategory.ELECTRONICS)
                .build());

        mockMvc.perform(get("/api/products")
                        .param("maxPrice", "100000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].name").value("저렴한 상품"));
    }
}
