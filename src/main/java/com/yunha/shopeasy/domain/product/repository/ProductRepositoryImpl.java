package com.yunha.shopeasy.domain.product.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yunha.shopeasy.domain.product.domain.Product;
import com.yunha.shopeasy.domain.product.domain.QProduct;
import com.yunha.shopeasy.domain.product.dto.request.ProductSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> search(ProductSearchCondition cond, Pageable pageable) {
        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(cond.keyword())) {
            builder.and(product.name.containsIgnoreCase(cond.keyword()));
        }
        if (cond.category() != null) {
            builder.and(product.category.eq(cond.category()));
        }
        if (cond.minPrice() != null) {
            builder.and(product.price.goe(cond.minPrice()));
        }
        if (cond.maxPrice() != null) {
            builder.and(product.price.loe(cond.maxPrice()));
        }

        List<Product> content = queryFactory
                .selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}