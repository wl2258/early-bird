package com.ssonzm.productservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.service.product.RedissonLockProductFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaReceiver {
    private final RedissonLockProductFacade redissonLockProductFacade;

    public KafkaReceiver(RedissonLockProductFacade redissonLockProductFacade) {
        this.redissonLockProductFacade = redissonLockProductFacade;
    }

    @KafkaListener(topics = KafkaVo.KAFKA_PRODUCT_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receiveRollback(ProductKafkaRollbackRespDto productKafkaRollbackRespDto) {
        log.error("[Product Consumer]: 상품 재고 롤백");
        redissonLockProductFacade.increaseProductQuantity(productKafkaRollbackRespDto);
    }
}
