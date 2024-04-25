package com.ssonzm.orderservice.service.delivery;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryRepository;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.service.order_product.OrderProductService;
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
        updateDeliveryStatus(
                orderProductService.findAllBetweenYesterdayAndToday().stream()
                        .map(OrderProduct::getId)
                        .toList(),
                DeliveryStatus.SHIPPED
        );
    }

    /**
     * 배송 상태 변경 DELIVERED
     */
    @Override
    @Transactional
    public void updateAllDeliveryStatusToDelivered() {
        log.info("Update Delivery Status to DELIVERED");
        updateDeliveryStatus(
                orderProductService.findAllBetweenTwoDaysAgoAndYesterday().stream()
                        .map(OrderProduct::getId)
                        .toList(),
                DeliveryStatus.DELIVERED
        );
    }

    private void updateDeliveryStatus(List<Long> orderProductIds, DeliveryStatus deliveryStatus) {
        List<Delivery> deliveryList = findDeliveryByOrderProductIds(orderProductIds);
        deliveryList.forEach(d -> d.updateDeliveryStatus(deliveryStatus));
    }

    @Override
    public List<Delivery> findDeliveryByOrderProductIds(List<Long> orderProductIds) {
        return deliveryRepository.findDeliveryByOrderProductIds(orderProductIds);
    }
}
