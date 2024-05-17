package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.event.OrderEventListener;
import com.ssonzm.orderservice.service.order.OrderInternalService;
import com.ssonzm.orderservice.service.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;

@Slf4j
@Service
public class KafkaReceiver {
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final OrderEventListener orderEventListener;
    private final OrderInternalService orderInternalService;

    public KafkaReceiver(OrderService orderService, OrderInternalService orderInternalService,
                         DeliveryService deliveryService, OrderEventListener orderEventListener) {
        this.orderService = orderService;
        this.orderInternalService = orderInternalService;
        this.deliveryService = deliveryService;
        this.orderEventListener = orderEventListener;
    }

    /**
     * [receive] 주문 엔티티 생성
     * product -> order
     * @param orderSaveKafkaReqDto
     */
    @KafkaListener(topics = KafkaVo.KAFKA_ORDER_TOPIC, containerFactory = "orderKafkaContainerFactory")
    public void receiveOrderSaveMessage(OrderSaveKafkaReqDto orderSaveKafkaReqDto) {
        log.debug("[Order Consumer]: 주문 엔티티 생성");

        orderInternalService.saveOrder(orderSaveKafkaReqDto);
    }

    /**
     * [receive] 결제 엔티티 생성 롤백
     * payment -> order
     * @param paymentKafkaRollbackRespDto
     */
    @KafkaListener(topics = KafkaVo.KAFKA_PAYMENT_ROLLBACK_TOPIC, containerFactory = "paymentRollbackKafkaContainerFactory")
    public void receivePaymentSaveRollbackMessage(PaymentKafkaRollbackRespDto paymentKafkaRollbackRespDto) {
        Long orderId = paymentKafkaRollbackRespDto.getOrderId();
        log.error("[Order Consumer(orderId = {})]: 결제 엔티티 생성 롤백", orderId);

        // 주문 취소
        orderService.updateOrderStatusByOrderId(orderId, OrderStatus.CANCELED);
    }
}
