package com.ssonzm.coremodule.dto.return_product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ReturnProductRequestDto {
    @Data
    @AllArgsConstructor
    public static class ReturnProductSaveReqDto {
        @NotNull(message = "반품할 상품을 선택해 주세요")
        private Long orderProductId;
        @NotNull(message = "반품 사유를 작성해 주세요")
        private String reason;
    }
}
