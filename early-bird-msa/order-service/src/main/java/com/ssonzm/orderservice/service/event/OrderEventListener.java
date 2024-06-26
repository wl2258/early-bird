package com.ssonzm.orderservice.service.event;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.orderservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class OrderEventListener {
    private final KafkaSender kafkaSender;

    public OrderEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    /**
     * [send] 결제 엔티티 생성 이벤트
     * order -> payment
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderSuccess(OrderEvent event) {
        Long orderId = event.getOrderId();
        OrderSaveKafkaReqDto orderSaveKafkaReqDto = event.getOrderSaveKafkaReqDto();

        log.debug("[Order producer(orderId = {})] 결제 엔티티 생성 이벤트 발행", orderId);

        kafkaSender.sendMessage(KafkaVo.KAFKA_PAYMENT_TOPIC,
                new PaymentRequestDto.PaymentSaveKafkaReqDto(
                        orderSaveKafkaReqDto.getUserId(),
                        orderId,
                        orderSaveKafkaReqDto.getProductId(),
                        orderSaveKafkaReqDto.getQuantity(),
                        orderSaveKafkaReqDto.getProductPrice()
                ));
    }

    /**
     * [send] 주문 생성 실패 시 롤백 이벤트
     * order -> product
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onOrderFail(OrderEvent event) {
        OrderSaveKafkaReqDto orderSaveKafkaReqDto = event.getOrderSaveKafkaReqDto();
        Long productId = orderSaveKafkaReqDto.getProductId();
        log.debug("[Product producer(productId = {})] 주문 생성 실패 롤백 이벤트 발행", productId);

        kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_ROLLBACK_TOPIC,
                new ProductResponseDto.ProductKafkaRollbackRespDto(
                        productId,
                        orderSaveKafkaReqDto.getQuantity())
        );
    }
}
