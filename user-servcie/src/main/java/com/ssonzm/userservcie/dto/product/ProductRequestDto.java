package com.ssonzm.userservcie.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;

public class ProductRequestDto {
    @Data
    public static class ProductSaveReqDto {
        @Size(max = 50, message = "상품명은 50자 이하로 작성해 주세요")
        @NotBlank(message = "상품명을 입력해 주세요")
        private String productName;

        @Pattern(regexp = "(FASHION|BEAUTY)$")
        private String category;

        @Size(max = 255, message = "내용은 255자 이하로 작성해 주세요")
        private String description;

        @NotNull(message = "수량을 입력해 주세요")
        private int quantity;

        @NotNull(message = "가격을 입력해 주세요")
        private int price;
    }
}
