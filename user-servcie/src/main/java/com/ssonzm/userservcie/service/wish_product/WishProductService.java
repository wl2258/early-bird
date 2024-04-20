package com.ssonzm.userservcie.service.wish_product;

import com.ssonzm.userservcie.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import com.ssonzm.userservcie.vo.wish_product.WishProductResponseVo.WishProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishProductService {
    Long saveWishProduct(Long userId, WishProductSaveReqDto wishProductSaveReqDto);

    Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable);
}
