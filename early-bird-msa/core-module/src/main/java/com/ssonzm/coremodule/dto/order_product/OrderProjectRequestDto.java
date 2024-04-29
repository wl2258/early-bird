package com.ssonzm.coremodule.dto.order_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderProjectRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductCancelReqDto {
        private Long orderProductId;
        private int quantity;
    }
}
