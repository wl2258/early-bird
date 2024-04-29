package com.ssonzm.coremodule.vo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductResponseVo {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductListRespVo {
        private Long productId;
        private Long userId;
        private String productName;
        private String category;
        private String productStatus;
        private int quantity;
        private int price;
        private String description;
    }
}
