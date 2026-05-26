package com.yunha.shopeasy.domain.product.controller;

import com.yunha.shopeasy.domain.product.domain.ProductCategory;
import com.yunha.shopeasy.domain.product.dto.request.CreateProductRequest;
import com.yunha.shopeasy.domain.product.dto.request.ProductSearchCondition;
import com.yunha.shopeasy.domain.product.dto.request.UpdateProductRequest;
import com.yunha.shopeasy.domain.product.dto.response.ProductDetailResponse;
import com.yunha.shopeasy.domain.product.dto.response.ProductResponse;

import com.yunha.shopeasy.domain.product.usecase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final SearchProductsUseCase searchProductsUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @PostMapping
    public ResponseEntity<ProductDetailResponse> create(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createProductUseCase.execute(request));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20) Pageable pageable) {
        ProductSearchCondition condition = new ProductSearchCondition(keyword, category, minPrice, maxPrice);
        return ResponseEntity.ok(searchProductsUseCase.execute(condition, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getDetail(@PathVariable Long id) {
        return ResponseEntity.ok(getProductUseCase.execute(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        updateProductUseCase.execute(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}