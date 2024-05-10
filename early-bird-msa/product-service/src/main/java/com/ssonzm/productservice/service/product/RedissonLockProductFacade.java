package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.service.event.ProductEvent;
import com.ssonzm.productservice.service.event.ProductEventListener;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;


@Slf4j
@Component
public class RedissonLockProductFacade {
    private final RedissonClient redissonClient;
    private final ProductService productService;
    private final ProductEventListener productEventListener;


    public RedissonLockProductFacade(RedissonClient redissonClient, ProductService productService,
                                     ProductEventListener productEventListener) {
        this.redissonClient = redissonClient;
        this.productService = productService;
        this.productEventListener = productEventListener;
    }

    private interface LockCallback<T> {
        T doInLock() throws InterruptedException;
    }

    public int getProductQuantity(Long productId) {
        return executeWithLock(productId, () -> productService.getProductQuantity(productId),
                "상품 재고 조회 LOCK 획득 실패");
    }

    public void decreaseProductQuantity(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();
        executeWithLock(productId, () -> {
            // 재고 확인
            productService.isLeftInStock(orderProductUpdateReqDto);

            // 주문 날짜 확인
            productService.isAvailableOrder(orderProductUpdateReqDto);

            // db 재고 감소
            Product product = productService.decreaseQuantity(userId, orderProductUpdateReqDto);

            productEventListener.onProductSuccess(
                    new ProductEvent(this, userId, product, orderProductUpdateReqDto));
            return null;
        },  "상품 재고 감소 LOCK 획득 실패");
    }

    public void increaseProductQuantity(Long productId, int quantity) {
        executeWithLock(productId, () -> {
            productService.increaseQuantity(productId, quantity);
            return null;
         },
"상품 재고 증가 LOCK 획득 실패");
    }

    private <T> T executeWithLock(Long productId, LockCallback<T> lockCallback, String logMsg) {
        RLock lock = redissonClient.getLock(String.valueOf(productId));
        try {
            boolean isLocked = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!isLocked) {
                log.debug(logMsg);
                throw new CommonBadRequestException("failLock");
            }
            return lockCallback.doInLock();

        } catch (InterruptedException e) {
            log.error("처리 중 에러 발생");
            throw new CommonBadRequestException("failQuantityUpdate");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
