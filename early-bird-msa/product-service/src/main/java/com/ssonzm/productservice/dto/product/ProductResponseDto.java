package com.ssonzm.productservice.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ssonzm.productservice.domain.product.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ProductResponseDto {
    @Data
    @NoArgsConstructor
    public static class ProductDetailsRespDto {
        private String name;
        private String category;
        private String description;
        private int quantity;
        private int price;
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdDate;

        public ProductDetailsRespDto(Product product) {
            this.name = product.getName();
            this.category = String.valueOf(product.getCategory());
            this.description = product.getDescription();
            this.quantity = product.getQuantity();
            this.price = product.getPrice();
            this.createdDate = product.getCreatedDate();
        }
    }
}
