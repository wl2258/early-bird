package com.ssonzm.userservcie.domain.wish_product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishProductRepository extends JpaRepository<WishProduct, Long>, WishProductRepositoryCustom {
    Optional<WishProduct> findByUserIdAndProductId(Long userId, Long productId);
}
