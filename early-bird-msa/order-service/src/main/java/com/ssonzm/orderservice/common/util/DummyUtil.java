package com.ssonzm.orderservice.common.util;

import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;

public class DummyUtil {
    public OrderProduct newOrderProduct(Long userId, Long productId, Order order, OrderStatus status) {
        return OrderProduct.builder()
                .order(order)
                .userId(userId)
                .status(status)
                .productId(productId)
                .quantity(1)
                .price(10000)
                .build();
    }

    public OrderProduct newMockOrderProduct(Long id, Long userId, Long productId, Order order, OrderStatus status) {
        return OrderProduct.builder()
                .order(order)
                .userId(userId)
                .status(status)
                .productId(productId)
                .quantity(1)
                .price(10000)
                .build();
    }

    public Order newOrder(Long userId) {
        return Order.builder()
                .userId(userId)
                .build();
    }

    public Order newMockOrder(Long id, Long userId) {
        return Order.builder()
                .id(id)
                .userId(userId)
                .build();
    }

    public Delivery newDelivery(Long orderProductId, DeliveryStatus status) {
        return Delivery.builder()
                .orderProductId(orderProductId)
                .status(status)
                .build();
    }

    public Delivery newMockDelivery(Long id, Long orderProductId, DeliveryStatus status) {
        return Delivery.builder()
                .id(id)
                .orderProductId(orderProductId)
                .status(status)
                .build();
    }
}
