package com.ssonzm.productservice.vo.product;

import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

public class ProductResponseVo {

    @Data
    @AllArgsConstructor
    public static class ProductListRespVo {
        private Long productId;
        private Long userId;
        private String productName;
        private ProductCategory category;
        private ProductStatus productStatus;
        private int quantity;
        private int price;
        private String description;
    }
}
