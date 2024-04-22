package com.ssonzm.userservcie.service.return_product;

import com.ssonzm.userservcie.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

public interface ReturnProductService {

    Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto);

    void updateReturnStatus();
}
