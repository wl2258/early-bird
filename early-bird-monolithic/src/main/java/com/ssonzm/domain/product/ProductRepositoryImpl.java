package com.ssonzm.domain.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssonzm.vo.product.ProductResponseVo.*;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ProductListRespVo> getProductList(Pageable pageable) {
        List<ProductListRespVo> content = queryFactory
                .select(Projections.constructor(ProductListRespVo.class,
                        QProduct.product.id,
                        QProduct.product.userId,
                        QProduct.product.name,
                        QProduct.product.category,
                        QProduct.product.status,
                        QProduct.product.quantity,
                        QProduct.product.price,
                        QProduct.product.description
                ))
                .from(QProduct.product)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QProduct.product.count())
                .from(QProduct.product);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

}
