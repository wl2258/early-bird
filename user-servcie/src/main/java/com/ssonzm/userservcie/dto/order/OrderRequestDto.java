package com.ssonzm.userservcie.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class OrderRequestDto {
    @Data
    public static class OrderSaveReqDto {
        @NotNull(message = "삼품을 선택해 주세요")
        private Long productId;
        @NotNull(message = "수량을 입력해 주세요")
        private int quantity;
    }
}
