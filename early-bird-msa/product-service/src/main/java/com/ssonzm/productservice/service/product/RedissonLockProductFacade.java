package com.ssonzm.productservice.service.product;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.domain.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import static com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;


@Slf4j
@Component
public class RedissonLockProductFacade {
    private final RedissonClient redissonClient;
    private final ProductService productService;

    public RedissonLockProductFacade(RedissonClient redissonClient, ProductService productService) {
        this.redissonClient = redissonClient;
        this.productService = productService;
    }

    public void decreaseProductQuantity(OrderProductUpdateReqDto orderProductUpdateReqDto) {
        Long productId = orderProductUpdateReqDto.getProductId();
        RLock lock = redissonClient.getLock(String.valueOf(productId));
        try {
            boolean isLocked = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!isLocked) {
                log.debug("상품 재고 감소 LOCK 획득 실패");
                throw new CommonBadRequestException("failLock");
            }

            // 재고 확인
            productService.isLeftInStock(orderProductUpdateReqDto);

            // 주문 날짜 확인
            productService.isAvailableOrder(orderProductUpdateReqDto);

            // db 재고 감소
            Product product = productService.decreaseQuantity(orderProductUpdateReqDto);

            productService.sendMessageToOrder(product, orderProductUpdateReqDto);

        } catch (InterruptedException e) {
            log.error("재고 감소 실패");
            throw new CommonBadRequestException("failQuantityUpdate");
        } finally {
            lock.unlock();
        }
    }

    public void increaseProductQuantity(ProductKafkaRollbackRespDto productKafkaRollbackRespDto) {
        Long productId = productKafkaRollbackRespDto.getProductId();
        RLock lock = redissonClient.getLock(String.valueOf(productId));
        try {
            boolean isLocked = lock.tryLock(5, 1, TimeUnit.SECONDS);

            if (!isLocked) {
                log.debug("상품 재고 증가 LOCK 획득 실패");
                throw new CommonBadRequestException("failLock");
            }

            productService.increaseQuantity(productKafkaRollbackRespDto.getProductId(),
                    productKafkaRollbackRespDto.getQuantity());
        } catch (InterruptedException e) {
            log.error("재고 증가 실패");
            throw new CommonBadRequestException("failQuantityUpdate");
        } finally {
            lock.unlock();
        }
    }
}
