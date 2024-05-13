package com.ssonzm.productservice.service.event;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProductEventListener {
    private final KafkaSender kafkaSender;

    public ProductEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductSuccess(ProductEvent event) {
        Long userId = event.getUserId();
        OrderProductUpdateReqDto orderProductUpdateReqDto = event.getOrderProductUpdateReqDto();

        log.debug("[Product producer(productId = {})] 주문 엔티티 생성 이벤트 발행",
                event.getOrderProductUpdateReqDto().getProductId());
        kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_TOPIC,
                createOrderSaveDto(userId, orderProductUpdateReqDto));
    }

    private OrderSaveKafkaReqDto createOrderSaveDto(Long userId,
                                                    OrderProductUpdateReqDto orderProductUpdateReqDto) {
        return new OrderSaveKafkaReqDto(
                userId,
                orderProductUpdateReqDto.getQuantity(),
                orderProductUpdateReqDto.getProductId(),
                orderProductUpdateReqDto.getPrice());
    }
}
