package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.ProductUpdateAfterOrderReqDto;
import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.productservice.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

public interface ProductService {
    Product saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    void saveProduct(Long userId, ProductSaveReqDto productSaveReqDto, MultipartFile file);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    List<ProductDetailsFeignClientRespDto> getProductDetailsByIds(List<Long> productIds);

    Product findProductByIdOrElseThrow(Long productId);

    Page<ProductListRespVo> getProductSavedByUser (Pageable pageable, Long userId);

    void updateProductQuantity(List<ProductUpdateAfterOrderReqDto> orderProductUpdateList);

    Long getProductQuantityByLua(Long productId);

    int getProductQuantity(Long productId);

    void updateProductInfo(ProductUpdateReqDto productUpdateReqDto);

    void isAvailableOrder(OrderProductUpdateReqDto orderProductUpdateReqDto);

    void isLeftInStock(OrderProductUpdateReqDto orderProductUpdateReqDto);

    void decreaseQuantity(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto);

    void decreaseQuantityByLua(OrderProductUpdateReqDto orderProductUpdateReqDto);

    void increaseQuantity(Long productId, int quantity);

    void increaseQuantityByLua(Long productId, Integer quantity);
}
