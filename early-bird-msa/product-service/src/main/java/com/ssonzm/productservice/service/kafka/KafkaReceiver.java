package com.ssonzm.productservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
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

    @KafkaListener(topics = KafkaVo.KAFKA_ORDER_ROLLBACK_TOPIC, containerFactory = "kafkaContainerFactory")
    public void receiveRollback(ProductKafkaRollbackRespDto productKafkaRollbackRespDto) {
        log.error("[Product Consumer]: 상품 재고 롤백");

        redissonLockProductFacade.increaseProductQuantity(
                productKafkaRollbackRespDto.getProductId(),
                productKafkaRollbackRespDto.getQuantity()
        );
    }

    /**
     * [receive] 결제 실패 롤백 (재고 증가)
     * payment -> product
     * @param paymentKafkaRollbackRespDto
     */
    @KafkaListener(topics = KafkaVo.KAFKA_PAYMENT_TO_PRODUCT_TOPIC, containerFactory = "paymentRollbackKafkaContainerFactory")
    public void receivePaymentSaveRollbackMessage(PaymentKafkaRollbackRespDto paymentKafkaRollbackRespDto) {
        Long productId = paymentKafkaRollbackRespDto.getProductId();
        log.error("[Product Consumer(productId = {})]: 결제 실패 롤백", productId);

        redissonLockProductFacade.increaseProductQuantity(productId, paymentKafkaRollbackRespDto.getQuantity());
    }
}
