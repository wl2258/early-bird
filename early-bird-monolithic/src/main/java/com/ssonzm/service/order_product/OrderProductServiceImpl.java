package com.ssonzm.service.order_product;

import com.ssonzm.common.exception.CommonBadRequestException;
import com.ssonzm.domain.delivery.Delivery;
import com.ssonzm.domain.delivery.DeliveryRepository;
import com.ssonzm.domain.delivery.DeliveryStatus;
import com.ssonzm.domain.order_product.OrderProduct;
import com.ssonzm.domain.order_product.OrderProductRepository;
import com.ssonzm.domain.order_product.OrderStatus;
import com.ssonzm.domain.product.Product;
import com.ssonzm.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderProductServiceImpl implements OrderProductService {
    private final ProductService productService;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderProductServiceImpl(ProductService productService,
                                   DeliveryRepository deliveryRepository,
                                   OrderProductRepository orderProductRepository) {
        this.productService = productService;
        this.deliveryRepository = deliveryRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    public List<OrderProduct> findAllByOrderIdOrElseThrow(Long orderId) {
        return orderProductRepository.findByOrderId(orderId);
    }

    @Override
    public OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId) {
        return orderProductRepository.findById(orderProductId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    @Override
    public List<OrderProduct> findAllBetweenYesterdayAndToday() {
        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime yesterday = today.minusDays(1);

        return orderProductRepository.findAllBetweenPrevDayAndToday(yesterday, today);
    }

    @Override
    public List<OrderProduct> findAllBetweenTwoDaysAgoAndYesterday() {
        LocalDateTime yesterday = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        LocalDateTime twoDaysAgo = yesterday.minusDays(1);

        return orderProductRepository.findAllBetweenPrevDayAndToday(twoDaysAgo, yesterday);
    }

    @Override
    @Transactional
    public void cancelOrderProduct(Long orderProductId) {
        OrderProduct orderProduct = findOrderProductByIdOrElseThrow(orderProductId);
        // 배송 중인 상품이 있는 경우: 주문 취소 불가
        if (isDeliveryInProgress(orderProduct)) {
            throw new CommonBadRequestException("failCancelOrder");
        }
        // 상품 재고 복구
        restoreProductQuantity(orderProduct);
        // 주문 상태 변경
        orderProduct.updateOrderStatus(OrderStatus.CANCELED);
    }

    private boolean isDeliveryInProgress(OrderProduct orderProduct) {
        Delivery findDelivery = deliveryRepository.findDeliveryByOrderProductId(orderProduct.getId())
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
        return !findDelivery.getStatus().equals(DeliveryStatus.READY_FOR_SHIPMENT);
    }

    private void restoreProductQuantity(OrderProduct orderProduct) {
        Product findProduct = productService.findProductByIdOrElseThrow(orderProduct.getProductId());
        findProduct.updateQuantity(orderProduct.getQuantity());
    }
}
