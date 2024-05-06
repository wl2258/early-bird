package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.productservice.domain.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import static com.ssonzm.coremodule.dto.product.ProductRequestDto.ProductSaveReqDto;
import static com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsRespDto;
import static com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;

public interface ProductService {
    Long saveProduct(Long userId, ProductSaveReqDto productSaveReqDto);

    Page<ProductListRespVo> getProductList(Pageable pageable);

    ProductDetailsRespDto getProductDetails(Long productId);

    List<ProductDetailsFeignClientRespDto> getProductDetailsByIds(List<Long> productIds);

    Product findProductByIdOrElseThrow(Long productId);

    Page<ProductListRespVo> getProductSavedByUser (Pageable pageable, Long userId);

    void updateProductQuantity(List<OrderProductUpdateReqDto> orderProductUpdateList);

    int getProductQuantity(Long productId);
    
    void updateProductInfo(ProductUpdateReqDto productUpdateReqDto);

    Product isAvailableOrder(OrderProductUpdateReqDto orderProductUpdateReqDto);

    void isLeftInStock(OrderProductUpdateReqDto orderProductUpdateReqDto);

    Product decreaseQuantity(OrderProductUpdateReqDto orderProductUpdateReqDto);

    void increaseQuantity(Long productId, int quantity);

    void sendMessageToOrder(Product product, Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto);
}
