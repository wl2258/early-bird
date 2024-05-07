package com.ssonzm.coremodule.dto.payment.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

public class PaymentRequestDto {
    @Data
    @AllArgsConstructor
    public static class PaymentSaveKafkaReqDto {
        private Long userId;
        private Long orderId;
        private Integer amount;
    }
}
