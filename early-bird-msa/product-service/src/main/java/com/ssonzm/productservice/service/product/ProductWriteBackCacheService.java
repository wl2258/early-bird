package com.ssonzm.productservice.service.product;

import com.ssonzm.productservice.domain.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class ProductWriteBackCacheService {
    private final ProductService productService;
    private final RedisTemplate<String, Integer> redisTemplate;


    public ProductWriteBackCacheService(ProductService productService,
                                        RedisTemplate<String, Integer> redisTemplate) {
        this.productService = productService;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    @Scheduled(fixedDelay = 1800000)
    public void writeBack() {
        log.debug("Start scheduling cache");
        Map<String, Integer> map = loadCacheData("product:*");
        updateProduct(map);
        log.debug("End scheduling cache");
    }

    private Map<String, Integer> loadCacheData(String pattern) {
        Set<String> redisKeys = getKeysByPrefix(pattern);
        Map<String, Integer> map = new HashMap<>();
        if (redisKeys != null && !redisKeys.isEmpty()) {
            for (String key : redisKeys) {
                Integer value = (Integer) redisTemplate.opsForValue().get(key);
                String productId = key.split(":")[1];

                map.put(productId, value);

                redisTemplate.delete(key);
            }
        }
        return map;
    }

    private Set<String> getKeysByPrefix(String pattern) {
        Set<String> keys = new HashSet<>();
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(200).build())) {
                while (cursor.hasNext()) {
                    keys.add(new String(cursor.next()));
                }
            }catch (Exception e){
                log.error("Redis: fail scan keys");
                throw new IllegalStateException();
            }
            return null;
        });
        return keys;
    }

    @Transactional
    public void updateProduct(Map<String, Integer> map) {
        map.entrySet().forEach(entry -> {
            String productId = entry.getKey();
            Product product = productService.findProductByIdOrElseThrow(Long.valueOf(productId));
            product.updateQuantity(entry.getValue());
        });
    }
}
