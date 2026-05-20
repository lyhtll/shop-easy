package com.yunha.shopeasy.domain.product.usecase;

import com.yunha.shopeasy.domain.product.dto.response.ProductDetailResponse;
import com.yunha.shopeasy.domain.product.error.ProductError;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;

    public ProductDetailResponse execute(Long id) {
        return productRepository.findById(id)
                .map(ProductDetailResponse::from)
                .orElseThrow(() -> new CustomException(ProductError.PRODUCT_NOT_FOUND));
    }
}