package com.ssonzm.productservice.service.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class ProductRedisService {
    private final String REDIS_PREFIX = "product:";
    private ValueOperations<String, Integer> valueOperations;
    private final RedisTemplate<String, Integer> redisTemplate;

    public ProductRedisService(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void saveProduct(Long productId, int quantity, long timeout, TimeUnit unit) {
        String key = REDIS_PREFIX + productId;
        valueOperations.setIfAbsent(key, quantity, timeout, unit); // 존재하지 않는 경우에 저장
        log.debug("상품이 등록되었습니다. 상품 ID: {}, 총 재고: {}", productId, quantity);
    }

    public Integer getProductQuantity(Long productId) {
        String key = REDIS_PREFIX + productId;
        return valueOperations.get(key);
    }

    public void increaseProductQuantity(Long productId, int quantity) {
        String key = REDIS_PREFIX + productId;
        valueOperations.increment(key, quantity);
        log.debug("상품 재고가 증가되었습니다. 상품 ID: {}, 증가된 수량: {}", productId, quantity);
    }

    public void decreaseProductQuantity(Long productId, int quantity) {
        String key = REDIS_PREFIX + productId;
        valueOperations.increment(key, -quantity);
        log.debug("상품 재고가 감소되었습니다. 상품 ID: {}, 감소된 수량: {}", productId, quantity);
    }

    public void deleteProduct(Long productId) {
        String key = REDIS_PREFIX + productId;
        redisTemplate.delete(key);
        log.debug("상품이 삭제되었습니다. 상품 ID: {}", productId);
    }
}
