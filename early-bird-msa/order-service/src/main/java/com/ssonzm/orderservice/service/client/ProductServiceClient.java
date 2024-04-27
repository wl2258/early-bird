package com.ssonzm.orderservice.service.client;

import com.ssonzm.orderservice.dto.product.ProductResponseDto.ProductDetailsRespDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/api/products/detail")
    List<ProductDetailsRespDto> getProductDetailsByIds(@RequestBody List<Long> productIds);
}
