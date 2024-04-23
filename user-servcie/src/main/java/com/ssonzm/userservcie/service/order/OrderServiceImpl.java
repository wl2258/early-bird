package com.ssonzm.userservcie.service.order;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.delivery.Delivery;
import com.ssonzm.userservcie.domain.delivery.DeliveryRepository;
import com.ssonzm.userservcie.domain.delivery.DeliveryStatus;
import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.domain.order.OrderRepository;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import com.ssonzm.userservcie.domain.order_product.OrderStatus;
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
import static com.ssonzm.userservcie.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;

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
        List<OrderProduct> orderProductList = createOrderProducts(userId, orderSaveReqDtoList, savedOrder);

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
    public Order findOrderByIdOrElseThrow(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }

    @Override
    public OrderDetailsRespDto getOrderDetail(Long orderId) {
        Order findOrder = findOrderByIdOrElseThrow(orderId);

        List<OrderProduct> orderProductList = orderProductService.findAllByOrderIdOrElseThrow(orderId);
        List<Long> orderProductIds = getOrderProductIds(orderProductList);

        List<DeliveryDetailsRespDto> deliveryRespDtos = getDeliveryDetailsRespDtos(orderProductIds);
        List<OrderProductDetailsRespDto> orderDetailsRespDtos = getOrderDetailsRespDtos(orderProductList);

        return getOrderDetailsRespDto(findOrder, deliveryRespDtos, orderDetailsRespDtos);
    }

    private List<OrderProductDetailsRespDto> getOrderDetailsRespDtos(List<OrderProduct> orderProductList) {
        return orderProductList.stream()
                .map(op -> new OrderProductDetailsRespDto(op.getId(), String.valueOf(op.getStatus())))
                .toList();
    }

    private List<Long> getOrderProductIds(List<OrderProduct> orderProductList) {
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

    private static OrderDetailsRespDto getOrderDetailsRespDto(Order findOrder,
                                                              List<DeliveryDetailsRespDto> deliveryRespDtos,
                                                              List<OrderProductDetailsRespDto> orderDetailsRespDtos) {
        return new OrderDetailsRespDto(findOrder, deliveryRespDtos, orderDetailsRespDtos);
    }

    private Order createOrder(Long userId) {
        return orderRepository.save(
                Order.builder()
                        .userId(userId)
                        .build());
    }

    private List<OrderProduct> createOrderProducts(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList, Order savedOrder) {
        return orderSaveReqDtoList.stream()
                .map(dto -> {
                    Product product = productService.findProductByIdOrElseThrow(dto.getProductId());
                    return createOrderProduct(userId, dto, product, savedOrder);
                })
                .toList();
    }

    private OrderProduct createOrderProduct(Long userId, OrderSaveReqDto orderSaveReqDto,
                                            Product product, Order savedOrder) {
        int quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .order(savedOrder)
                .userId(userId)
                .productId(orderSaveReqDto.getProductId())
                .quantity(quantity)
                .price(quantity * product.getPrice())
                .status(OrderStatus.CREATED)
                .build();
    }

    private int calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToInt(OrderProduct::getPrice)
                .sum();
    }
}
