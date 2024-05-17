package com.ssonzm.orderservice.service.client;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    Logger log = LoggerFactory.getLogger(ProductServiceClient.class);

    @PostMapping("/internal/products/detail")
    @CircuitBreaker(name = "productCircuitBreaker", fallbackMethod = "failGetProductDetailsByIds")
    ResponseEntity<ResponseDto<List<ProductDetailsFeignClientRespDto>>> getProductDetailsByIds(@RequestBody List<Long> productIds);

    default ResponseEntity<ResponseDto<List<ProductDetailsFeignClientRespDto>>> failGetProductDetailsByIds(Exception e) {
        log.error("[ProductServiceClient] getProductDetailsByIds() 호출 실패");
        throw new CommonBadRequestException("tryAgain");
    }
}
