package com.yunha.shopeasy.domain.product.usecase;

import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.dto.request.CreateProductRequest;
import com.yunha.shopeasy.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;

    @Transactional
    public Long execute(CreateProductRequest request) {
        return productRepository.save(Product.builder()
                .name(request.name())
                .price(request.price())
                .stock(request.stock())
                .category(request.category())
                .description(request.description())
                .build()).getId();
    }
}