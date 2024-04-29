package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.productservice.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductListSavedUser;
import static com.ssonzm.productservice.vo.product.ProductResponseVo.ProductListRespVo;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    List<ProductDetailsFeignClientRespDto> getProductDetailsByIds(List<Long> productIds);

    Product findProductByIdOrElseThrow(Long productId);

    ProductListSavedUser getProductSavedByUser (Long userId);
}
