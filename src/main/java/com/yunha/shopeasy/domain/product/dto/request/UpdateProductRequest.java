package com.yunha.shopeasy.domain.product.dto.request;

import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        String name,
        @Positive BigDecimal price,
        @PositiveOrZero Integer stock,
        ProductCategory category,
        String description
) {}