package com.ssonzm.domain.wish_product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.vo.wish_product.WishProductResponseVo.*;

public interface WishProductRepositoryCustom {
    Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable);
}
