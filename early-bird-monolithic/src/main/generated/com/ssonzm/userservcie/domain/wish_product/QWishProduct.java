package com.ssonzm.userservcie.domain.wish_product;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWishProduct is a Querydsl query type for WishProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishProduct extends EntityPathBase<WishProduct> {

    private static final long serialVersionUID = 1358785564L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWishProduct wishProduct = new QWishProduct("wishProduct");

    public final com.ssonzm.userservcie.domain.common.QBaseEntity _super = new com.ssonzm.userservcie.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final com.ssonzm.userservcie.domain.product.QProduct product;

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QWishProduct(String variable) {
        this(WishProduct.class, forVariable(variable), INITS);
    }

    public QWishProduct(Path<? extends WishProduct> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWishProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWishProduct(PathMetadata metadata, PathInits inits) {
        this(WishProduct.class, metadata, inits);
    }

    public QWishProduct(Class<? extends WishProduct> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.ssonzm.userservcie.domain.product.QProduct(forProperty("product")) : null;
    }

}

