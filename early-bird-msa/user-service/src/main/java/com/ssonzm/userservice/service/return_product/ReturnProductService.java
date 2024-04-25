package com.ssonzm.userservice.service.return_product;

import com.ssonzm.userservice.dto.return_product.ReturnProductRequestDto.ReturnProductSaveReqDto;

public interface ReturnProductService {

    Long saveReturn(Long userId, ReturnProductSaveReqDto returnProductSaveReqDto);

    void updateReturnStatus();
}
