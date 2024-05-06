package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class RedissonLockProductFacade {
    private final RedissonClient redissonClient;
    private final ProductService productService;

    public RedissonLockProductFacade(RedissonClient redissonClient, ProductService productService) {
        this.redissonClient = redissonClient;
        this.productService = productService;
    }

    public void decreaseProductQuantity(OrderProductRequestDto.OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();
        RLock lock = redissonClient.getLock(String.valueOf(productId));
        try {
            boolean isLocked = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!isLocked) {
                log.debug("상품 재고 감소 LOCK 획득 실패");
                return;
            }

            // 재고 확인
            productService.isLeftInStock(orderProductUpdateReqDto);

            // 주문 날짜 확인
            productService.isAvailableOrder(orderProductUpdateReqDto);

            // db 재고 감소
            productService.decreaseQuantity(orderProductUpdateReqDto);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
