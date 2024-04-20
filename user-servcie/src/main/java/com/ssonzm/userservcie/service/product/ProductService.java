package com.ssonzm.userservcie.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;
import static com.ssonzm.userservcie.vo.product.ProductResponseVo.*;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);
}
