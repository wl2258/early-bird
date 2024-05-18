package com.ssonzm.service.delivery;

import com.ssonzm.domain.delivery.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery findDeliveryByOrderProductIdOrElseThrow(Long orderProductId);

    List<Delivery> findDeliveryByOrderProductIds(List<Long> orderProductIds);

    void updateAllDeliveryStatusToShipped();

    void updateAllDeliveryStatusToDelivered();
}
