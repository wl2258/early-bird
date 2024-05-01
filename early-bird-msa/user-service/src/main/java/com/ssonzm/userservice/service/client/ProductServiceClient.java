package com.ssonzm.userservice.service.client;


import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @GetMapping("/internal/products/my/{userId}")
    ResponseEntity<ResponseDto<Page<ProductListRespVo>>> getProductListSavedByUser(
            @PathVariable("userId") Long userId);


    @GetMapping("/internal/wish-products/{userId}")
    ResponseEntity<ResponseDto<Page<WishProductListRespVo>>> getWishProductList(
            @PathVariable("userId") Long userId);


}
