package com.ssonzm.coremodule.dto.payment.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

public class PaymentResponseDto {
    @Data
    @AllArgsConstructor
    public static class PaymentKafkaRollbackRespDto {
        private Long orderId;
    }
}
