package com.yunha.shopeasy.domain.product.usecase;

import com.yunha.shopeasy.domain.product.dto.request.ProductSearchCondition;
import com.yunha.shopeasy.domain.product.dto.response.ProductResponse;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchProductsUseCase {

    private final ProductRepository productRepository;

    public Page<ProductResponse> execute(ProductSearchCondition condition, Pageable pageable) {
        return productRepository.search(condition, pageable).map(ProductResponse::from);
    }
}