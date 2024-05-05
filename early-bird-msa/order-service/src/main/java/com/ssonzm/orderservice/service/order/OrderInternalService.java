package com.ssonzm.orderservice.service.order;

import com.ssonzm.coremodule.dto.order.kafka.OrderRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderInternalService {
    private final OrderRepository orderRepository;

    public OrderInternalService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        orderRepository.save(createOrder(orderSaveReqDto));
    }

    private static Order createOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        return Order.builder()
                .userId(orderSaveReqDto.getUserId())
                .totalPrice(orderSaveReqDto.getTotalPrice())
                .build();
    }
}