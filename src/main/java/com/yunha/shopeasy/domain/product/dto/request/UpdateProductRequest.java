package com.yunha.shopeasy.domain.product.dto.request;

import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record UpdateProductRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal price,
        @NotNull @PositiveOrZero int stock,
        @NotNull ProductCategory category,
        String description
) {}