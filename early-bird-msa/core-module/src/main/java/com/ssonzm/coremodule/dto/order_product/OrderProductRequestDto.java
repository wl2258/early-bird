package com.ssonzm.coremodule.dto.order_product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderProductRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductUpdateReqDto {
        private Long productId;
        private int quantity;
    }
}
