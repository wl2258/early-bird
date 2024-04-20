package com.ssonzm.userservcie.service.product;

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;
import static com.ssonzm.userservcie.dto.product.ProductResponseDto.*;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    ProductDetailsRespDto getProductDetails(Long productId);
}
