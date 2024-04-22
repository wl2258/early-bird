package com.ssonzm.userservcie.service.delivery;

import com.ssonzm.userservcie.domain.delivery.Delivery;

public interface DeliveryService {
    Delivery findDeliveryByOrderProductIdOrElseThrow(Long deliveryId);

    void updateAllDeliveryStatusToShipped();

    void updateAllDeliveryStatusToDelivered();
}
