package com.ssonzm.coremodule.dto.order_product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderProductRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductUpdateReqDto {
        @NotNull
        private Long userId;
        @NotNull(message = "상품 아이디를 입력해 주세요")
        private Long productId;
        @NotNull(message = "구매 수량을 입력해 주세요")
        private Integer quantity;
        @NotNull(message = "상품 가격을 입력해 주세요")
        private Integer price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductUpdateAfterOrderReqDto {
        @NotNull(message = "상품 아이디를 입력해 주세요")
        private Long productId;
        @NotNull(message = "구매 수량을 입력해 주세요")
        private Integer quantity;
    }
}
