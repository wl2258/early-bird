package com.ssonzm.coremodule.dto.payment.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSaveKafkaReqDto {
        private Long userId;
        private Long orderId;
        private Long productId;
        private Integer quantity;
        private Integer price;
    }
}
