package com.yunha.shopeasy.domain.product.service;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.error.ProductError;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import com.yunha.shopeasy.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new CustomException(ProductError.PRODUCT_NOT_FOUND));
    }
}
