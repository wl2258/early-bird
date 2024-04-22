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
import com.ssonzm.userservcie.service.delivery.DeliveryService;
import com.ssonzm.userservcie.service.order_product.OrderProductService;
import com.ssonzm.userservcie.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssonzm.userservcie.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import static com.ssonzm.userservcie.dto.order.OrderResponseDto.OrderDetailsRespDto;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductService orderProductService;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(ProductService productService, DeliveryService deliveryService,
                            OrderRepository orderRepository, DeliveryRepository deliveryRepository,
                            OrderProductService orderProductService, OrderProductRepository orderProductRepository) {
        this.productService = productService;
        this.deliveryService = deliveryService;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderProductService = orderProductService;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    @Transactional
    public Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList) {
        Order savedOrder = createOrder(userId);
        List<OrderProduct> orderProductList = createOrderProducts(orderSaveReqDtoList, savedOrder);

        orderProductRepository.saveAll(orderProductList);

        int totalPrice = calculateTotalPrice(orderProductList);
        savedOrder.updateTotalPrice(totalPrice);

        saveDeliveryList(orderProductList); // 배송 준비 중 상태로 Delivery 저장

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

        List<OrderProduct> orderProductList = orderProductService.findAllByOrderIdOrElseThrow(orderId);

        // 배송 중인 상품이 있는 경우: 주문 취소 불가
        checkDeliveryStatus(orderProductList);
        // 상품 재고 복구
        restoreProductQuantity(orderProductList);
        // 주문 상태 변경
        findOrder.updateOrderStatus(OrderStatus.CANCELED);
    }

    private void restoreProductQuantity(List<OrderProduct> orderProductList) {
        for (OrderProduct op : orderProductList) {
            Product findProduct = productService.findProductByIdOrElseThrow(op.getProductId());
            findProduct.updateQuantity(op.getQuantity());
        }
    }

    private void checkDeliveryStatus(List<OrderProduct> orderProductList) {
        List<Long> orderProductIds = orderProductList.stream()
                .map(OrderProduct::getId)
                .toList();

        List<Delivery> deliveryList = deliveryService.findDeliveryByOrderProductIds(orderProductIds);

        deliveryList.forEach(d -> {
            if (!d.getStatus().equals(DeliveryStatus.READY_FOR_SHIPMENT)) {
                throw new CommonBadRequestException("failCancelOrder");
            }
        });

        deliveryList.forEach(d -> d.updateDeliveryStatus(DeliveryStatus.CANCELED));
    }

    @Override
    public Order findOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    @Override
    public OrderDetailsRespDto getOrderDetail(Long orderId) {
        Order findOrder = findOrderByIdOrElseThrow(orderId);

        List<Long> orderProductIds = getOrderProductIds(orderId);

        List<DeliveryDetailsRespDto> deliveryRespDtos = getDeliveryDetailsRespDtos(orderProductIds);

        return getOrderDetailsRespDto(findOrder, deliveryRespDtos);
    }

    private List<Long> getOrderProductIds(Long orderId) {
        List<OrderProduct> orderProductList = orderProductService.findAllByOrderIdOrElseThrow(orderId);
        return orderProductList.stream()
                .map(OrderProduct::getId)
                .toList();
    }

    private List<DeliveryDetailsRespDto> getDeliveryDetailsRespDtos(List<Long> orderProductIds) {
        List<Delivery> deliveryList = deliveryService.findDeliveryByOrderProductIds(orderProductIds);
        return deliveryList.stream()
                .map(d -> new DeliveryDetailsRespDto(d.getId(), String.valueOf(d.getStatus())))
                .toList();
    }

    private static OrderDetailsRespDto getOrderDetailsRespDto(Order findOrder, List<DeliveryDetailsRespDto> deliveryRespDtos) {
        return OrderDetailsRespDto.builder()
                .orderStatus(String.valueOf(findOrder.getStatus()))
                .deliveryStatus(deliveryRespDtos)
                .createdDate(findOrder.getCreatedDate())
                .totalPrice(findOrder.getTotalPrice())
                .build();
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
                .mapToInt(OrderProduct::getPrice)
                .sum();
    }
}
