package com.ssonzm.coremodule.dto.product.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProductRequestDto {
    @Data
    @AllArgsConstructor
    public static class OrderSaveKafkaReqDto {
        private Long userId;
        private Integer quantity;
        private Long productId;
        private Integer productPrice;
    }
}