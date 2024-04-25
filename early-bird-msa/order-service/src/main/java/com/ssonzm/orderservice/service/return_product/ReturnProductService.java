package com.ssonzm.orderservice.service.return_product;

import com.ssonzm.orderservice.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

public interface ReturnProductService {

    Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto);

    void updateReturnStatus();
}
