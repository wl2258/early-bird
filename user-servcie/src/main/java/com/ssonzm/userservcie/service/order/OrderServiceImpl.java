package com.ssonzm.userservcie.service.order;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.delivery.Delivery;
import com.ssonzm.userservcie.domain.delivery.DeliveryRepository;
import com.ssonzm.userservcie.domain.delivery.DeliveryStatus;
import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.domain.order.OrderRepository;
import com.ssonzm.userservcie.domain.order.OrderStatus;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.userservcie.service.product.ProductService;
import com.ssonzm.userservcie.service.schedule.DeliveryStatusSchedulerImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductRepository orderProductRepository;
    private final DeliveryStatusSchedulerImpl deliveryStatusScheduler;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, DeliveryRepository deliveryRepository, OrderProductRepository orderProductRepository, DeliveryStatusSchedulerImpl deliveryStatusScheduler) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderProductRepository = orderProductRepository;
        this.deliveryStatusScheduler = deliveryStatusScheduler;
    }

    @Override
    @Transactional
    public Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList) {
        Order savedOrder = createOrder(userId);
        List<OrderProduct> orderProductList = createOrderProducts(orderSaveReqDtoList, savedOrder);
        int totalPrice = calculateTotalPrice(orderProductList);

        orderProductRepository.saveAll(orderProductList);
        savedOrder.updateTotalPrice(totalPrice);

        saveDeliveryList(orderProductList);

        return savedOrder.getId();
    }

    private void saveDeliveryList(List<OrderProduct> orderProductList) {
        List<Delivery> deliveryList = orderProductList.stream()
                .map(this::createDelivery)
                .toList();
        deliveryRepository.saveAll(deliveryList);
    }

    private Delivery createDelivery(OrderProduct orderProduct) {
        return Delivery.builder()
                .orderProductId(orderProduct.getId())
                .status(DeliveryStatus.READY_FOR_SHIPMENT)
                .build();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order findOrder = findOrderByIdOrElseThrow(orderId);
        // TODO 배송 중인 상품인 경우 주문 취소 불가능, 취소 후 재고 복구
        findOrder.updateOrderStatus(OrderStatus.CANCELED);
    }

    @Override
    public Order findOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    private Order createOrder(Long userId) {
        return orderRepository.save(
                Order.builder()
                        .userId(userId)
                        .status(OrderStatus.CREATED)
                        .build());
    }

    private List<OrderProduct> createOrderProducts(List<OrderSaveReqDto> orderSaveReqDtoList, Order savedOrder) {
        return orderSaveReqDtoList.stream()
                .map(dto -> {
                    Product product = productService.findProductByIdOrElseThrow(dto.getProductId());
                    return createOrderProduct(dto, product, savedOrder);
                })
                .toList();
    }

    private OrderProduct createOrderProduct(OrderSaveReqDto orderSaveReqDto, Product product, Order savedOrder) {
        int quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .orderId(savedOrder.getId())
                .productId(orderSaveReqDto.getProductId())
                .quantity(quantity)
                .price(quantity * product.getPrice())
                .build();
    }

    private int calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToInt(orderProduct -> orderProduct.getQuantity() * orderProduct.getPrice())
                .sum();
    }
}
