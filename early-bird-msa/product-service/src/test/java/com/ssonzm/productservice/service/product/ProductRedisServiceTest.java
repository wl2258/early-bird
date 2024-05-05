package com.ssonzm.productservice.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRedisServiceTest {
    @Autowired
    private ProductRedisService productRedisService;

    private Long productId;
    private final int initialStock = 100;
    private final int timeout = 5;
    private final TimeUnit unit = TimeUnit.MINUTES;

    @BeforeEach
    void setUp() {
        productId = 100L;
        productRedisService.saveProduct(productId, initialStock, timeout, unit);
    }

    @Test
    void increaseQuantityTest() {
        productRedisService.increaseProductQuantity(productId, 5);
        int quantity = productRedisService.getProductQuantity(productId);
        assertThat(quantity).isEqualTo(initialStock + 5);
    }

    @Test
    void decreaseQuantityTest() {
        productRedisService.decreaseProductQuantity(productId, 5);
        int quantity = productRedisService.getProductQuantity(productId);
        assertThat(quantity).isEqualTo(initialStock - 5);
    }

    @Test
    void getQuantityTest() {
        Integer productQuantity = productRedisService.getProductQuantity(productId);
        assertThat(productQuantity).isEqualTo(initialStock);
    }
}