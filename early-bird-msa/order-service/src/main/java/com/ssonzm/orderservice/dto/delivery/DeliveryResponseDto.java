package com.ssonzm.orderservice.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;

public class DeliveryResponseDto {

    @Data
    @AllArgsConstructor
    public static class DeliveryDetailsRespDto {
        private Long id;
        private String deliveryStatus;
    }
}
