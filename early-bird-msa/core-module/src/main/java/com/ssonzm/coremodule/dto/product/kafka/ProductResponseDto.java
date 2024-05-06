package com.ssonzm.coremodule.dto.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProductResponseDto {
    @Data
    @AllArgsConstructor
    public static class ProductKafkaRespDto {
        private Long productId;
        private Integer quantity;
    }
}
