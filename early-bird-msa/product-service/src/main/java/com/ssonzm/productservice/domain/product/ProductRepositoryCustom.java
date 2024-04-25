package com.ssonzm.productservice.domain.product;

import com.ssonzm.productservice.vo.product.ProductResponseVo.ProductListRespVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductListRespVo> getProductList(Pageable pageable);
}
