package com.ssonzm.orderservice.service.order;

import com.ssonzm.coremodule.dto.order.OrderResponseDto.OrderSaveRespDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderInternalService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderInternalService(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public OrderSaveRespDto saveOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        Order order = orderRepository.save(createOrder(orderSaveReqDto));
        OrderProduct orderProduct = orderProductRepository.save(createOrderProduct(order, orderSaveReqDto));
        return new OrderSaveRespDto(order.getId(), orderProduct.getId());
    }

    private OrderProduct createOrderProduct(Order order, OrderSaveKafkaReqDto orderSaveReqDto) {
        Integer quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .userId(orderSaveReqDto.getUserId())
                .productId(orderSaveReqDto.getProductId())
                .price(orderSaveReqDto.getProductPrice() * quantity)
                .quantity(quantity)
                .order(order)
                .build();
    }

    private static Order createOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        return Order.builder()
                .userId(orderSaveReqDto.getUserId())
                .totalPrice(orderSaveReqDto.getProductPrice() * orderSaveReqDto.getQuantity())
                .build();
    }
}