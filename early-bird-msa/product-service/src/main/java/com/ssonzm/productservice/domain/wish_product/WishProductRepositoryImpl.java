package com.ssonzm.productservice.domain.wish_product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.ssonzm.productservice.domain.product.QProduct.product;
import static com.ssonzm.productservice.domain.wish_product.QWishProduct.wishProduct;
import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.*;


public class WishProductRepositoryImpl implements WishProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable) {
        List<WishProductListRespVo> content = queryFactory
                .select(Projections.constructor(WishProductListRespVo.class,
                        wishProduct.id,
                        wishProduct.product.id,
                        wishProduct.product.name,
                        wishProduct.quantity,
                        wishProduct.price
                ))
                .from(wishProduct)
                .join(wishProduct.product, product)
                .where(wishProduct.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(wishProduct.count())
                .from(wishProduct);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
