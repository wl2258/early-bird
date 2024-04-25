package com.ssonzm.orderservice.domain.product;

import com.ssonzm.orderservice.vo.product.ProductResponseVo.ProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductListRespVo> getProductList(Pageable pageable);
}
