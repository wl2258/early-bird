package com.ssonzm.orderservice.service.event;

import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import org.springframework.context.ApplicationEvent;

public class OrderEvent extends ApplicationEvent {
    private Long orderId;
    private OrderSaveKafkaReqDto orderSaveKafkaReqDto;

    public OrderEvent(Object source, OrderSaveKafkaReqDto orderSaveKafkaReqDto) {
        super(source);
        this.orderSaveKafkaReqDto = orderSaveKafkaReqDto;
    }

    public OrderEvent(Object source, Long orderId, OrderSaveKafkaReqDto orderSaveKafkaReqDto) {
        super(source);
        this.orderId = orderId;
        this.orderSaveKafkaReqDto = orderSaveKafkaReqDto;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderSaveKafkaReqDto getOrderSaveKafkaReqDto() {
        return orderSaveKafkaReqDto;
    }
}
