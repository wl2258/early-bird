package com.ssonzm.coremodule.dto.payment.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentKafkaRollbackRespDto {
        private Long orderId;
    }
}
