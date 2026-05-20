package com.yunha.shopeasy.domain.product.dto.response;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.domain.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetailResponse(
        Long id,
        String name,
        BigDecimal price,
        int stock,
        ProductCategory category,
        String description,
        LocalDateTime createdAt
) {
    public static ProductDetailResponse from(Product product) {
        return new ProductDetailResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getDescription(),
                product.getCreatedAt()
        );
    }
}