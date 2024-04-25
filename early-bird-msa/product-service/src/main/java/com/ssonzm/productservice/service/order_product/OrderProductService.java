package com.ssonzm.productservice.service.order_product;

import com.ssonzm.productservice.domain.order_product.OrderProduct;

import java.util.List;

public interface OrderProductService {
    List<OrderProduct> findAllByOrderIdOrElseThrow(Long orderId);

    OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId);

    List<OrderProduct> findAllBetweenYesterdayAndToday();

    List<OrderProduct> findAllBetweenTwoDaysAgoAndYesterday();

    void cancelOrderProduct(Long orderProductId);
}
