package com.ssonzm.coremodule.dto.order_product;

import lombok.AllArgsConstructor;
import lombok.Data;

public class OrderProductResponseDto {
    @Data
    @AllArgsConstructor
    public static class OrderProductDetailsRespDto {
        private Long id;
        private String orderStatus;
        private String productName;
        private int quantity;
        private int price;
    }

    @Data
    @AllArgsConstructor
    public static class OrderProductUserRespDto {
        private Long orderProductId;
        private String productName;
        private int quantity;
        private int price;
        private String orderStatus;
        private String deliveryStatus;
    }
}
