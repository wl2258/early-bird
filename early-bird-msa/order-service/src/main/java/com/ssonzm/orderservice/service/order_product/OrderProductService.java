package com.ssonzm.orderservice.service.order_product;

import com.ssonzm.orderservice.domain.order_product.OrderProduct;

import java.util.List;

public interface OrderProductService {
    List<OrderProduct> findAllByOrderIdOrElseThrow(Long orderId);

    OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId);

    List<OrderProduct> findAllBetweenYesterdayAndToday();

    List<OrderProduct> findAllBetweenTwoDaysAgoAndYesterday();

    void cancelOrderProduct(Long orderProductId);
}
