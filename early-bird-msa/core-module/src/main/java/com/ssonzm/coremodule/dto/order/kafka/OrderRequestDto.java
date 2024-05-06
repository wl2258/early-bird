package com.ssonzm.coremodule.dto.order.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

public class OrderRequestDto {
    @Data
    @AllArgsConstructor
    public static class OrderSaveKafkaReqDto {
        private Long UserId;
        private Integer quantity;
        private Long productId;
        private Integer productPrice;
    }
}