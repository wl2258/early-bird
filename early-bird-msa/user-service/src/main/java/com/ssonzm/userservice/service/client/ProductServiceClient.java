package com.ssonzm.userservice.service.client;


import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.vo.product.ProductResponseVo.ProductListRespVo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

import static com.ssonzm.coremodule.vo.wish_product.WishProductResponseVo.WishProductListRespVo;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    @GetMapping("/internal/products/my/{userId}")
    @CircuitBreaker(name = "myProductListCircuitBreaker", fallbackMethod = "failGetMyProductList")
    ResponseEntity<ResponseDto<Page<ProductListRespVo>>> getProductListSavedByUser(
            @PathVariable("userId") Long userId);

    default ResponseEntity<ResponseDto<Page<ProductListRespVo>>> failGetMyProductList(Exception e) {
        log.error("[ProductServiceClient] getProductListSavedByUser() 호출 실패");

        ResponseDto<Page<ProductListRespVo>> responseDto = new ResponseDto<>();
        responseDto.setBody(new PageImpl<>(Collections.emptyList()));

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
    }

    @GetMapping("/internal/wish-products/{userId}")
    @CircuitBreaker(name = "myWishProductCircuitBreaker", fallbackMethod = "failGetMyWishProductList")
    ResponseEntity<ResponseDto<Page<WishProductListRespVo>>> getWishProductList(
            @PathVariable("userId") Long userId);

    default ResponseEntity<ResponseDto<Page<WishProductListRespVo>>> failGetMyWishProductList(Exception e) {
        log.error("[ProductServiceClient] getWishProductList() 호출 실패");

        ResponseDto<Page<WishProductListRespVo>> responseDto = new ResponseDto<>();
        responseDto.setBody(new PageImpl<>(Collections.emptyList()));

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(responseDto);
    }
}
