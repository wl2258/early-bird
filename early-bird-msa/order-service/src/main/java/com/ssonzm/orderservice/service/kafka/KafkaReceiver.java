package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.order.OrderInternalService;
import com.ssonzm.orderservice.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.dto.order.OrderResponseDto.OrderSaveRespDto;
import static com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;

@Slf4j
@Service
public class KafkaReceiver {
    private final KafkaSender kafkaSender;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final OrderInternalService orderInternalService;

    public KafkaReceiver(KafkaSender kafkaSender, OrderService orderService,
                         OrderInternalService orderInternalService, DeliveryService deliveryService) {
        this.kafkaSender = kafkaSender;
        this.orderService = orderService;
        this.orderInternalService = orderInternalService;
        this.deliveryService = deliveryService;
    }

    @KafkaListener(topics = KafkaVo.KAFKA_ORDER_TOPIC, containerFactory = "orderKafkaContainerFactory")
    public void receiveOrderSaveMessage(OrderSaveKafkaReqDto orderSaveKafkaReqDto) {
        log.debug("[Order Consumer]: 주문 엔티티 생성");

        Long deliveryId = null;
        boolean isSuccess = false;
        OrderSaveRespDto orderSaveRespDto = new OrderSaveRespDto();
        try {
            isSuccess = true;
            orderSaveRespDto = orderInternalService.saveOrder(orderSaveKafkaReqDto);
            deliveryId = deliveryService.saveDelivery(orderSaveRespDto.getOrderProductId());
        } catch (Exception e) {
            isSuccess = false;
            handleException(orderSaveKafkaReqDto, orderSaveRespDto, deliveryId);
        }

        // 주문, 배송 엔티티 저장 성공
        if (isSuccess) {
            sendMessageToKafkaPaymentTopic(orderSaveKafkaReqDto, orderSaveRespDto);
        }
    }

    private void handleException(OrderSaveKafkaReqDto orderSaveKafkaReqDto, OrderSaveRespDto orderSaveRespDto, Long deliveryId) {
        log.error("[Order Consumer]: Rollback");

        // 롤백 메시지 전송
        sendMessageToKafkaOrderRollbackTopic(orderSaveKafkaReqDto, orderSaveRespDto);

        // 주문, 배송 상태 변경
        Long orderProductId = orderSaveRespDto.getOrderProductId();
        if (orderProductId != null) orderService.updateOrderStatus(orderProductId, OrderStatus.CANCELED);
        if (deliveryId != null) deliveryService.updateDeliveryStatus(deliveryId, DeliveryStatus.CANCELED);
    }

    /**
     * 주문 생성 실페 시 롤백 이벤트
     * @param orderSaveKafkaReqDto
     * @param orderSaveRespDto
     */
    private void sendMessageToKafkaOrderRollbackTopic(OrderSaveKafkaReqDto orderSaveKafkaReqDto, OrderSaveRespDto orderSaveRespDto) {
        kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_ROLLBACK_TOPIC,
                new ProductKafkaRollbackRespDto(
                        orderSaveRespDto.getOrderId(),
                        orderSaveKafkaReqDto.getProductId(),
                        orderSaveKafkaReqDto.getQuantity())
        );
    }

    /**
     * 결제 엔티티 생성 이벤트
     * @param orderSaveKafkaReqDto
     * @param orderSaveRespDto
     */
    private void sendMessageToKafkaPaymentTopic(OrderSaveKafkaReqDto orderSaveKafkaReqDto, OrderSaveRespDto orderSaveRespDto) {
        kafkaSender.sendMessage(KafkaVo.KAFKA_PAYMENT_TOPIC,
                new PaymentSaveKafkaReqDto(
                        orderSaveKafkaReqDto.getUserId(),
                        orderSaveRespDto.getOrderId(),
                        orderSaveKafkaReqDto.getProductPrice() * orderSaveKafkaReqDto.getQuantity()
                ));
    }
}
