package com.ssonzm.productservice.service.product;

import com.ssonzm.productservice.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.ssonzm.productservice.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.productservice.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.productservice.dto.product.ProductResponseDto.ProductListSavedUser;
import static com.ssonzm.productservice.vo.product.ProductResponseVo.ProductListRespVo;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    Product findProductByIdOrElseThrow(Long productId);

    ProductListSavedUser getProductSavedByUser (Long userId);
}
