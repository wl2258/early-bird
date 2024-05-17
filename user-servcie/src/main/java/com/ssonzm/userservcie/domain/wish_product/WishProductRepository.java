package com.ssonzm.userservcie.domain.wish_product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishProductRepository extends JpaRepository<WishProduct, Long>, WishProductRepositoryCustom {
    Optional<WishProduct> findByUserIdAndProductId(Long userId, Long productId);

    @Query("select wp from WishProduct wp join fetch wp.product p where wp.id = :wishProductId")
    Optional<WishProduct> findByIdProductFetchJoin(@Param("wishProductId")Long wishProductId);
}
