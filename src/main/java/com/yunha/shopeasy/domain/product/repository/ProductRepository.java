package com.yunha.shopeasy.domain.product.repository;

import com.yunha.shopeasy.domain.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {}