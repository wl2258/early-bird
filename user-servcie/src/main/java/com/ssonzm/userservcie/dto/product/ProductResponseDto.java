package com.ssonzm.userservcie.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

public class ProductResponseDto {
    @Data
    public static class ProductDetailsRespDto {
        private String name;
        private String category;
        private String description;
        private int quantity;
        private int price;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;
    }
}
