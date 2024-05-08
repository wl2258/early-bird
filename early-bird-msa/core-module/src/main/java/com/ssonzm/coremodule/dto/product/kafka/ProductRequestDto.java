package com.ssonzm.coremodule.dto.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderSaveKafkaReqDto {
        private Long userId;
        private Integer quantity;
        private Long productId;
        private Integer productPrice;
    }
}