package com.ssonzm.userservcie.service.product;

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);
}
