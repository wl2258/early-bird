package com.ssonzm.userservice.vo.wish_product;

import lombok.AllArgsConstructor;
import lombok.Data;

public class WishProductResponseVo {

    @Data
    @AllArgsConstructor
    public static class WishProductListRespVo {
        private Long wishProductId;
        private Long productId;
        private String name;
        private int quantity;
        private int price;
    }
}
