package com.ssonzm.productservice.service.return_product;

import com.ssonzm.productservice.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

public interface ReturnProductService {

    Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto);

    void updateReturnStatus();
}
