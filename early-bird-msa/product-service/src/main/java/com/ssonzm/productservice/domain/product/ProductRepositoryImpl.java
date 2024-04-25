package com.ssonzm.productservice.domain.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssonzm.productservice.domain.product.QProduct.*;
import static com.ssonzm.productservice.vo.product.ProductResponseVo.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductListRespVo> getProductList(Pageable pageable) {
        List<ProductListRespVo> content = queryFactory
                .select(Projections.constructor(ProductListRespVo.class,
                        product.id,
                        product.userId,
                        product.name,
                        product.category,
                        product.status,
                        product.quantity,
                        product.price,
                        product.description
                ))
                .from(product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
