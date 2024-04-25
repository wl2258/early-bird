package com.ssonzm.productservice.domain.wish_product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.productservice.vo.wish_product.WishProductResponseVo.*;

public interface WishProductRepositoryCustom {
    Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable);
}
