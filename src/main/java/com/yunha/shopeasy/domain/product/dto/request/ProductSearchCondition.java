package com.yunha.shopeasy.domain.product.dto.request;

import com.yunha.shopeasy.domain.product.domain.ProductCategory;

import java.math.BigDecimal;

public record ProductSearchCondition(
        String keyword,
        ProductCategory category,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {}