package com.yunha.shopeasy.domain.product.usecase;

import com.yunha.shopeasy.domain.product.error.ProductError;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public void execute(Long id) {
        productRepository.delete(
                productRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ProductError.PRODUCT_NOT_FOUND))
        );
    }
}