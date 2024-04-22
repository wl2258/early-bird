package com.ssonzm.userservcie.service.delivery;

import com.ssonzm.userservcie.domain.delivery.Delivery;

import java.util.List;

public interface DeliveryService {
    Delivery findDeliveryByOrderProductIdOrElseThrow(Long orderProductId);

    List<Delivery> findDeliveryByOrderProductIds(List<Long> orderProductIds);

    void updateAllDeliveryStatusToShipped();

    void updateAllDeliveryStatusToDelivered();
}
