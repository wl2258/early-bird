package com.ssonzm.userservcie.service.product;

import com.ssonzm.userservcie.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.userservcie.dto.product.ProductRequestDto.*;
import static com.ssonzm.userservcie.vo.product.ProductResponseVo.*;
import static com.ssonzm.userservcie.dto.product.ProductResponseDto.*;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    Product findProductByIdOrElseThrow(Long productId);
}
