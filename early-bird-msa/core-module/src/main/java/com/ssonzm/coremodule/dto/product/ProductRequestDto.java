package com.ssonzm.coremodule.dto.product;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ProductRequestDto {
    @Data
    @AllArgsConstructor
    public static class ProductSaveReqDto {
        @Size(max = 50, message = "상품명은 50자 이하로 작성해 주세요")
        @NotBlank(message = "상품명을 입력해 주세요")
        private String productName;

        @Pattern(regexp = "(FASHION|BEAUTY)$")
        private String category;

        @Size(max = 255, message = "내용은 255자 이하로 작성해 주세요")
        private String description;

        @NotNull(message = "수량을 입력해 주세요")
        private Integer quantity;

        @NotNull(message = "가격을 입력해 주세요")
        private Integer price;

        @Nullable
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        @FutureOrPresent(message = "예약 시작 시간은 현재 이후로 설정해 주세요")
        private LocalDateTime reservationStartTime;
    }
}
