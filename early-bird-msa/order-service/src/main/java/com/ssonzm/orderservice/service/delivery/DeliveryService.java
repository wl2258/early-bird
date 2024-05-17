package com.ssonzm.orderservice.service.delivery;

import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;

import java.util.List;

public interface DeliveryService {
    Delivery findDeliveryByOrderProductIdOrElseThrow(Long orderProductId);

    List<Delivery> findDeliveryByOrderProductIds(List<Long> orderProductIds);

    void updateAllDeliveryStatusToShipped();

    void updateAllDeliveryStatusToDelivered();

    Long saveDelivery(Long orderProductId);

    void updateDeliveryStatus(Long deliveryId, DeliveryStatus deliveryStatus);
}
