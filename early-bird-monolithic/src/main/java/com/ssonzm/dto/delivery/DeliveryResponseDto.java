package com.ssonzm.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Data;

public class DeliveryResponseDto {
    public DeliveryResponseDto(Long id, String s) {
    }

    @Data
    @AllArgsConstructor
    public static class DeliveryDetailsRespDto {
        private Long id;
        private String deliveryStatus;
    }
}
