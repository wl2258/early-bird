package com.ssonzm.service.product;

import com.ssonzm.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.vo.product.ProductResponseVo.ProductListRespVo;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    Product findProductByIdOrElseThrow(Long productId);
}
