package com.ssonzm.userservice.domain.product;

import com.ssonzm.userservice.vo.product.ProductResponseVo.ProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductListRespVo> getProductList(Pageable pageable);
}
