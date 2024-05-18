package com.ssonzm.vo.product;

import com.ssonzm.domain.product.ProductCategory;
import com.ssonzm.domain.product.ProductStatus;
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
