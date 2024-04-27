package com.ssonzm.orderservice.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductResponseDto {
    @Data
    @NoArgsConstructor
    public static class ProductDetailsRespDto {
        private Long id;
        private String name;
        private String category;
        private String description;
        private int quantity;
        private int price;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
    }
}
