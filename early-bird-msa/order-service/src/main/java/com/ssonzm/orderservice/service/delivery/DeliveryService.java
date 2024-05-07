package com.ssonzm.orderservice.service.delivery;

import com.ssonzm.orderservice.domain.delivery.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery findDeliveryByOrderProductIdOrElseThrow(Long orderProductId);

    List<Delivery> findDeliveryByOrderProductIds(List<Long> orderProductIds);

    void updateAllDeliveryStatusToShipped();

    void updateAllDeliveryStatusToDelivered();

    void saveDelivery(Long orderProductId);
}
