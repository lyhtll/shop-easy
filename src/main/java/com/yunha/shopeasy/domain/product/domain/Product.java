package com.yunha.shopeasy.domain.product.domain;

import com.yunha.shopeasy.domain.product.error.ProductError;
import com.yunha.shopeasy.global.common.BaseEntity;
import com.yunha.shopeasy.global.error.CustomException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    private String description;

    @Version
    private Long version;

    @Builder
    public Product(String name, BigDecimal price, int stock, ProductCategory category, String description) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.description = description;
    }

    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new CustomException(ProductError.OUT_OF_STOCK);
        }
        this.stock -= quantity;
    }

    public void update(String name, BigDecimal price, Integer stock, ProductCategory category, String description) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (stock != null) this.stock = stock;
        if (category != null) this.category = category;
        if (description != null) this.description = description;
    }
}