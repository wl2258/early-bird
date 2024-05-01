package com.ssonzm.coremodule.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ProductResponseDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailsRespDto {
        private Long productId;
        private String username;
        private String productName;
        private String category;
        private String description;
        private int quantity;
        private int price;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime reservationStartTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductDetailsFeignClientRespDto {
        private Long id;
        private int price;
        private LocalDateTime reservationStartTime;
    }

    @Data
    @AllArgsConstructor
    public static class ProductListSavedUser {
        private List<ProductDetailsRespDto> savedProductList;
    }
}
