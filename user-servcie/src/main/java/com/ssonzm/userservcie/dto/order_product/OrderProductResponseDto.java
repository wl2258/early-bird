package com.ssonzm.userservcie.dto.order_product;

import lombok.AllArgsConstructor;
import lombok.Data;

public class OrderProductResponseDto {
    @Data
    @AllArgsConstructor
    public static class OrderProductDetailsRespDto {
        private Long id;
        private String orderStatus;
    }
}
