package com.ssonzm.orderservice.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class OrderRequestDto {
    @Data
    @AllArgsConstructor
    public static class OrderSaveReqDto {
        @NotNull(message = "삼품을 선택해 주세요")
        private Long productId;
        @NotNull(message = "수량을 입력해 주세요")
        private Integer quantity;
    }
}
