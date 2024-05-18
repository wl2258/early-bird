package com.ssonzm.domain.wish_product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssonzm.domain.product.QProduct;
import com.ssonzm.vo.wish_product.WishProductResponseVo.WishProductListRespVo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


public class WishProductRepositoryImpl implements WishProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public WishProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable) {
        List<WishProductListRespVo> content = queryFactory
                .select(Projections.constructor(WishProductListRespVo.class,
                        QWishProduct.wishProduct.id,
                        QWishProduct.wishProduct.product.id,
                        QWishProduct.wishProduct.product.name,
                        QWishProduct.wishProduct.quantity,
                        QWishProduct.wishProduct.price
                ))
                .from(QWishProduct.wishProduct)
                .join(QWishProduct.wishProduct.product, QProduct.product)
                .where(QWishProduct.wishProduct.userId.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(QWishProduct.wishProduct.count())
                .from(QWishProduct.wishProduct);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
