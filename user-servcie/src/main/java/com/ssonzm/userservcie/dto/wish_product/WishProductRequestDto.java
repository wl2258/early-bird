package com.ssonzm.userservcie.dto.wish_product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class WishProductRequestDto {
    @Data
    public static class WishProductSaveReqDto {
        @NotNull(message = "상품을 선택해 주세요")
        private Long productId;
        @NotNull(message = "상품 수량을 입력해 주세요")
        private int quantity;
    }
}
