package com.ssonzm.userservcie.service.wish_product;

import com.ssonzm.userservcie.dto.wish_product.WishProductRequestDto.WishProductSaveReqDto;

public interface WishProductService {
    void saveWishProduct(Long userId, WishProductSaveReqDto wishProductSaveReqDto);
}
