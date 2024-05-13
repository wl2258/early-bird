package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.service.event.ProductEvent;
import com.ssonzm.productservice.service.event.ProductEventListener;
import com.ssonzm.productservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;


@Slf4j
@Component
public class RedissonLockProductFacade {
    private final KafkaSender kafkaSender;
    private final RedissonClient redissonClient;
    private final ProductService productService;
    private final ProductEventListener productEventListener;

    public RedissonLockProductFacade(KafkaSender kafkaSender,
                                     RedissonClient redissonClient,
                                     ProductService productService,
                                     ProductEventListener productEventListener) {
        this.kafkaSender = kafkaSender;
        this.redissonClient = redissonClient;
        this.productService = productService;
        this.productEventListener = productEventListener;
    }

    private interface LockCallback<T> {
        T doInLock() throws InterruptedException;
    }

    public Long getProductQuantity(Long productId) {
        return productService.getProductQuantityByLua(productId);

        /*return executeWithLock(productId, () -> productService.getProductQuantity(productId),
                "상품 재고 조회 LOCK 획득 실패");*/
    }

    public void decreaseQuantityWithLock(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();
        executeWithLock(productId, () -> {
            // 재고 확인
            productService.isLeftInStock(orderProductUpdateReqDto);

            // 주문 날짜 확인
            productService.isAvailableOrder(orderProductUpdateReqDto);

            // 재고 감소
            productService.decreaseQuantity(userId, orderProductUpdateReqDto);

            productEventListener.onProductSuccess(new ProductEvent(this, userId, orderProductUpdateReqDto));
            return null;
        }, "상품 재고 감소 LOCK 획득 실패");
    }

    public void decreaseProductQuantity(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto) {

        // 주문 날짜 확인
        productService.isAvailableOrder(orderProductUpdateReqDto);

        // 재고 감소 by Lua
        productService.decreaseQuantityByLua(orderProductUpdateReqDto);

        // Produce Event to order-service
//        productEventListener.onProductSuccess(new ProductEvent(this, userId, orderProductUpdateReqDto));

        /**
         * write back 전략을 사용하면서 굳이 커밋될 때까지 기다릴 필요 없으므로
         * 트랜잭션 리스너 사용해서 이벤트 발행할 필요 없음
         */
        kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_TOPIC,
                createOrderSaveDto(userId, orderProductUpdateReqDto));
    }

    private OrderSaveKafkaReqDto createOrderSaveDto(Long userId, OrderProductUpdateReqDto orderProductUpdateReqDto) {
        return new OrderSaveKafkaReqDto(
                userId,
                orderProductUpdateReqDto.getQuantity(),
                orderProductUpdateReqDto.getProductId(),
                orderProductUpdateReqDto.getPrice());
    }

    public void increaseProductQuantity(Long productId, int quantity) {
        productService.increaseQuantityByLua(productId, quantity);
    }

    public void increaseQuantityByLock(Long productId, int quantity) {
        executeWithLock(productId, () -> {
                    productService.increaseQuantity(productId, quantity);
                    return null;
                }, "상품 재고 증가 LOCK 획득 실패");
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
