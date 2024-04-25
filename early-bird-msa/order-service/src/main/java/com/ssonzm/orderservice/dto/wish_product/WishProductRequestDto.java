package com.ssonzm.orderservice.dto.wish_product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WishProductRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishProductSaveReqDto {
        @NotNull(message = "상품을 선택해 주세요")
        private Long productId;
        @NotNull(message = "상품 수량을 입력해 주세요")
        private int quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishProductUpdateReqDto {
        @NotNull(message = "관심 상품을 선택해 주세요")
        private Long wishProductId;
        @NotNull(message = "상품을 선택해 주세요")
        private Long productId;
        @NotNull(message = "상품 수량을 입력해 주세요")
        private int quantity;
    }
}
