package com.ssonzm.productservice.common.util;

import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.domain.product.ProductCategory;
import com.ssonzm.productservice.domain.product.ProductStatus;
import com.ssonzm.productservice.domain.wish_product.WishProduct;

import java.time.LocalDateTime;

public class DummyUtil {
    public Product newProduct(String name, ProductStatus status, Long userId) {
        return Product.builder()
                .name(name)
                .price(10000)
                .status(status)
                .userId(userId)
                .quantity(10000)
                .description("")
                .build();
    }

    public Product newMockProduct(Long id, String name, ProductStatus status, Long userId) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(10000)
                .status(status)
                .userId(userId)
                .quantity(10000)
                .description("test description")
                .category(ProductCategory.FASHION)
                .reservationStartTime(LocalDateTime.now().minusDays(1))
                .build();
    }

    public Product newMockProduct(Long id, String name, ProductStatus status, Long userId,
                                  int quantity, LocalDateTime reservationStartTime) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(10000)
                .status(status)
                .userId(userId)
                .quantity(quantity)
                .description("test description")
                .category(ProductCategory.FASHION)
                .reservationStartTime(reservationStartTime)
                .build();
    }

    public WishProduct newMockWishProduct(Long id, Long userId, Product product) {
        return WishProduct.builder()
                .id(id)
                .userId(userId)
                .product(product)
                .quantity(1)
                .price(product.getPrice())
                .build();
    }

}
