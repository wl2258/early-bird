package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
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


    public void decreaseQuantityInRedisByLua(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();
        Integer quantity = orderProductUpdateReqDto.getQuantity();

        String script = """
                local productKey = KEYS[1]
                local quantity = tonumber(ARGV[1])
                        
                local currentQuantity = tonumber(redis.call('GET', productKey))
                if currentQuantity == nil then
                    return -2
                end
                if currentQuantity < quantity then
                    return -1 -- 재고 부족
                end
                        
                redis.call('DECRBY', productKey, quantity)
                return 1 -- 재고 감소 성공
                        """;

        Long result = (Long) redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId.toString()), quantity);

        if (result == -1) {
            throw new CommonBadRequestException("outOfStock");
        } else if (result == -2) {
            throw new CommonBadRequestException("notFoundData");
        }

        log.debug("[상품 재고 감소 성공] productId = {}", productId);
    }


    public void increaseQuantityInRedisByLua(Long productId, Integer quantity) {
        String script = """
                local productKey = KEYS[1]
                local quantity = tonumber(ARGV[1])
                                
                local currentQuantity = tonumber(redis.call('GET', productKey))
                if currentQuantity == nil then
                    return -1
                else
                    redis.call('INCRBY', productKey, quantity)
                end
                                
                return 1
                        """;

        Long result = (Long) redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId.toString()), quantity);

        if (result == -1) {
            throw new CommonBadRequestException("notFoundData");
        }

        log.debug("[상품 재고 증가 성공] productId = {}", productId);
    }


    public Long getQuantityInRedisByLua(Long productId) {
        String script = """
                local productKey = KEYS[1]

                -- 재고 확인
                local currentQuantity = tonumber(redis.call('GET', productKey))
                if currentQuantity == nil then
                    return -1
                end
                                
                -- 재고 반환
                return currentQuantity
                """;

        Long result = (Long) redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(REDIS_PREFIX + productId.toString()));

        if (result == -1) {
            throw new CommonBadRequestException("notFoundData");
        }

        return result;
    }
}
