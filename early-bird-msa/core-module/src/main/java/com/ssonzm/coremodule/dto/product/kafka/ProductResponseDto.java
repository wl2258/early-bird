package com.ssonzm.coremodule.dto.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductKafkaRollbackRespDto {
        private Long productId;
        private Integer quantity;
    }
}
