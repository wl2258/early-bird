package com.ssonzm.userservcie.service.schedule;

import com.ssonzm.userservcie.service.delivery.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryStatusSchedulerImpl {
    private final DeliveryService deliveryService;

    public DeliveryStatusSchedulerImpl(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    /**
     * AM 2:00
     * 하루 전 주문: 배송 상태 SHIPPED
     * 이틀 전 주문: 배송 상태 DELIVERED
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateScheduleToShipped() {
        deliveryService.updateAllDeliveryStatusToShipped();
        deliveryService.updateAllDeliveryStatusToDelivered();
    }
}