package com.ssonzm.orderservice.service.order;

import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryRepository;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderProductRepository;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.service.event.OrderEvent;
import com.ssonzm.orderservice.service.event.OrderEventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderInternalService {
    private final OrderRepository orderRepository;
    private final OrderEventListener orderEventListener;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductRepository orderProductRepository;


    public OrderInternalService(OrderRepository orderRepository, OrderEventListener orderEventListener,
                                DeliveryRepository deliveryRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderEventListener = orderEventListener;
        this.deliveryRepository = deliveryRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Transactional
    public void saveOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        Order order = orderRepository.save(createOrder(orderSaveReqDto));
        OrderProduct orderProduct = orderProductRepository.save(createOrderProduct(order, orderSaveReqDto));
        deliveryRepository.save(createDelivery(orderProduct.getId()));

        orderEventListener.onOrderSuccess(new OrderEvent(this, order.getId(), orderSaveReqDto));
    }

    private Delivery createDelivery(Long orderProductId) {
        return Delivery.builder()
                .orderProductId(orderProductId)
                .status(DeliveryStatus.READY_FOR_SHIPMENT)
                .build();
    }

    private OrderProduct createOrderProduct(Order order, OrderSaveKafkaReqDto orderSaveReqDto) {
        Integer quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .userId(orderSaveReqDto.getUserId())
                .productId(orderSaveReqDto.getProductId())
                .order(order)
                .quantity(quantity)
                .price(orderSaveReqDto.getProductPrice() * quantity)
                .status(OrderStatus.CREATED)
                .build();
    }

    private static Order createOrder(OrderSaveKafkaReqDto orderSaveReqDto) {
        return Order.builder()
                .userId(orderSaveReqDto.getUserId())
                .totalPrice(orderSaveReqDto.getProductPrice() * orderSaveReqDto.getQuantity())
                .build();
    }
}