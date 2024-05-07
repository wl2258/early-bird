package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.order.OrderInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;

@Slf4j
@Service
public class KafkaReceiver {
    private final KafkaSender kafkaSender;
    private final DeliveryService deliveryService;
    private final OrderInternalService orderService;

    public KafkaReceiver(KafkaSender kafkaSender, OrderInternalService orderService, DeliveryService deliveryService) {
        this.kafkaSender = kafkaSender;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = KafkaVo.KAFKA_PRODUCT_TOPIC, containerFactory = "orderKafkaContainerFactory")
    public void receiveOrderSaveMessage(OrderSaveKafkaReqDto orderSaveKafkaReqDto) {
        log.debug("[Order Consumer]: 주문 엔티티 생성");

        try {
            Long orderProductId = orderService.saveOrder(orderSaveKafkaReqDto);
            deliveryService.saveDelivery(orderProductId);
        } catch (Exception e) {
            log.error("[Order Consumer]: Rollback");
            kafkaSender.sendMessage(KafkaVo.KAFKA_PRODUCT_ROLLBACK_TOPIC,
                    new ProductKafkaRollbackRespDto(
                            orderSaveKafkaReqDto.getProductId(),
                            orderSaveKafkaReqDto.getQuantity())
            );
        }
    }
}
