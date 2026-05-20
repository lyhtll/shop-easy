package com.yunha.shopeasy.domain.product.repository;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.dto.request.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> search(ProductSearchCondition condition, Pageable pageable);
}