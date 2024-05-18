package com.ssonzm.userservcie.domain.product;

import com.ssonzm.userservcie.vo.product.ProductResponseVo.ProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductListRespVo> getProductList(Pageable pageable);
}
