package com.ssonzm.paymentservice.dto;

import com.ssonzm.paymentservice.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentSaveRespDto {
        private Long paymentId;
        private PaymentStatus paymentStatus;
    }
}
