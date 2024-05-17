package com.ssonzm.coremodule.dto.payment.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentKafkaRollbackRespDto {
        private Long orderId;
        private Long productId;
        private Integer quantity;
    }

    public static PaymentKafkaRollbackRespDto createPaymentKafkaRollbackRespDto(
            PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        return new PaymentKafkaRollbackRespDto(
                paymentSaveKafkaReqDto.getOrderId(),
                paymentSaveKafkaReqDto.getProductId(),
                paymentSaveKafkaReqDto.getQuantity()
        );
    }
}
