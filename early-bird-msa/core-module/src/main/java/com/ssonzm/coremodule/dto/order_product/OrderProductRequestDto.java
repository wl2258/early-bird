package com.ssonzm.coremodule.dto.order_product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class OrderProductRequestDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderProductUpdateReqDto {
        @NotNull(message = "상품 아이디를 입력해 주세요")
        private Long productId;
        @NotNull(message = "구매 수량을 입력해 주세요")
        @Size(min = 1)
        private Integer quantity;
    }
}
