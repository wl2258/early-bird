package com.ssonzm.userservcie.service.order_product;

import com.ssonzm.userservcie.domain.order_product.OrderProduct;

import java.util.List;

public interface OrderProductService {
    List<OrderProduct> findAllByOrderIdOrElseThrow(Long orderId);

    OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId);

    List<OrderProduct> findAllBetweenYesterdayAndToday();

    List<OrderProduct> findAllBetweenTwoDaysAgoAndYesterday();
}
