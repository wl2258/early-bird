package com.ssonzm.userservice.service.wish_product;

import com.ssonzm.userservice.domain.wish_product.WishProduct;
import com.ssonzm.userservice.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;
import com.ssonzm.userservice.dto.wish_product.WishProductRequestDto.WishProductUpdateReqDto;
import com.ssonzm.userservice.vo.wish_product.WishProductResponseVo.WishProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishProductService {
    Long saveWishProduct(Long userId, WishProductSaveReqDto wishProductSaveReqDto);

    Page<WishProductListRespVo> findWishProductList(Long userId, Pageable pageable);

    void updateQuantity(WishProductUpdateReqDto wishProductUpdateReqDto);

    WishProduct findWishProductOrThrow(Long wishProductId);

    void deleteWishProduct(Long wishProductId);
}
