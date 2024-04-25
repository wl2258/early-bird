package com.ssonzm.userservice.service.schedule;

import com.ssonzm.userservice.service.delivery.DeliveryService;
import com.ssonzm.userservice.service.return_product.ReturnProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {
    private final DeliveryService deliveryService;
    private final ReturnProductService returnProductService;

    public SchedulerService(DeliveryService deliveryService, ReturnProductService returnProductService) {
        this.deliveryService = deliveryService;
        this.returnProductService = returnProductService;
    }

    /**
     * AM 2:00
     * 하루 전 주문: 배송 상태 SHIPPED
     * 이틀 전 주문: 배송 상태 DELIVERED
     * 하루 전 반품 요청: 반품 상태 APPROVED
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateDeliveryStatus() {
        deliveryService.updateAllDeliveryStatusToShipped();
        deliveryService.updateAllDeliveryStatusToDelivered();
        returnProductService.updateReturnStatus();
    }
}