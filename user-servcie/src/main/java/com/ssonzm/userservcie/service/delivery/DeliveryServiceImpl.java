package com.ssonzm.userservcie.service.delivery;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.delivery.Delivery;
import com.ssonzm.userservcie.domain.delivery.DeliveryRepository;
import com.ssonzm.userservcie.domain.delivery.DeliveryStatus;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.service.order_product.OrderProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderProductService orderProductService;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, OrderProductService orderProductService) {
        this.deliveryRepository = deliveryRepository;
        this.orderProductService = orderProductService;
    }

    @Override
    public Delivery findDeliveryByOrderProductIdOrElseThrow(Long orderProductId) {
        return deliveryRepository.findDeliveryByOrderProductId(orderProductId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    /**
     * 배송 상태 변경 SHIPPED
     */
    @Override
    @Transactional
    public void updateAllDeliveryStatusToShipped() {
        log.info("Update Delivery Status to SHIPPED");
        List<OrderProduct> orderProductList = orderProductService.findAllBetweenYesterdayAndToday();
        List<Long> orderProductIds = orderProductList.stream()
                .map(OrderProduct::getId)
                .toList();

        updateDeliveryStatus(orderProductIds, DeliveryStatus.SHIPPED);
    }

    /**
     * 배송 상태 변경 DELIVERED
     */
    @Override
    @Transactional
    public void updateAllDeliveryStatusToDelivered() {
        log.info("Update Delivery Status to DELIVERED");
        List<OrderProduct> orderProductList = orderProductService.findAllBetweenTwoDaysAgoAndYesterday();
        List<Long> orderProductIds = orderProductList.stream()
                .map(OrderProduct::getId)
                .toList();
        updateDeliveryStatus(orderProductIds, DeliveryStatus.DELIVERED);
    }

    private void updateDeliveryStatus(List<Long> orderProductIds, DeliveryStatus deliveryStatus) {
        List<Delivery> deliveryList = deliveryRepository.findDeliveryByOrderProductIds(orderProductIds);
        deliveryList.forEach(d -> d.updateDeliveryStatus(deliveryStatus));
    }
}
