package com.ssonzm.orderservice.service.client;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @PostMapping("/internal/products/detail")
    ResponseEntity<ResponseDto<List<ProductDetailsFeignClientRespDto>>> getProductDetailsByIds(@RequestBody List<Long> productIds);
}
