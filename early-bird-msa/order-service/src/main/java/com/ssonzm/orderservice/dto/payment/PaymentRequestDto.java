package com.ssonzm.orderservice.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class PaymentRequestDto {
    @Data
    public static class PaymentSaveReqDto {
        @NotNull(message = "결제할 주문을 선택해 주세요")
        private Long orderId;
    }
}
