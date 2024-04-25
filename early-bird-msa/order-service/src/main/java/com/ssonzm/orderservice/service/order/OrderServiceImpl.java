package com.ssonzm.orderservice.service.order;

import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryRepository;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderProductRepository;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.order_product.OrderProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssonzm.orderservice.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import static com.ssonzm.orderservice.dto.order.OrderResponseDto.OrderDetailsRespDto;
import static com.ssonzm.orderservice.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductService orderProductService;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(DeliveryService deliveryService, OrderRepository orderRepository,
                            DeliveryRepository deliveryRepository, OrderProductService orderProductService,
                            OrderProductRepository orderProductRepository) {
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

        saveDeliveryList(orderProductList);
        return savedOrder.getId();
    }

    private Order createOrder(Long userId) {
        return orderRepository.save(Order.builder().userId(userId).build());
    }

    private List<OrderProduct> createOrderProducts(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList, Order savedOrder) {
        return orderSaveReqDtoList.stream()
                // TODO 수정 필요
                .map(dto -> createOrderProduct(userId, dto, 0, savedOrder))
                .toList();
    }

    private OrderProduct createOrderProduct(Long userId, OrderSaveReqDto orderSaveReqDto, int price, Order savedOrder) {
        int quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .order(savedOrder)
                .userId(userId)
                .productId(orderSaveReqDto.getProductId())
                .quantity(quantity)
                .price(quantity * price)
                .status(OrderStatus.CREATED)
                .build();
    }

    private void saveDeliveryList(List<OrderProduct> orderProductList) {
        List<Delivery> deliveryList = orderProductList.stream()
                .map(op -> Delivery.builder()
                        .orderProductId(op.getId())
                        .status(DeliveryStatus.READY_FOR_SHIPMENT)
                        .build())
                .toList();
        deliveryRepository.saveAll(deliveryList);
    }

    private int calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .mapToInt(OrderProduct::getPrice)
                .sum();
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

        List<DeliveryDetailsRespDto> deliveryRespDtos = getDeliveryDetails(orderProductIds);
        List<OrderProductDetailsRespDto> orderDetailsRespDtos = getOrderDetails(orderProductList);

        return getOrderDetailsResult(findOrder, deliveryRespDtos, orderDetailsRespDtos);
    }

    private List<OrderProductDetailsRespDto> getOrderDetails(List<OrderProduct> orderProductList) {
        return orderProductList.stream()
                .map(op -> new OrderProductDetailsRespDto(op.getId(), String.valueOf(op.getStatus())))
                .toList();
    }

    private List<Long> getOrderProductIds(List<OrderProduct> orderProductList) {
        return orderProductList.stream()
                .map(OrderProduct::getId)
                .toList();
    }

    private List<DeliveryDetailsRespDto> getDeliveryDetails(List<Long> orderProductIds) {
        List<Delivery> deliveryList = deliveryService.findDeliveryByOrderProductIds(orderProductIds);
        return deliveryList.stream()
                .map(d -> new DeliveryDetailsRespDto(d.getId(), String.valueOf(d.getStatus())))
                .toList();
    }

    private static OrderDetailsRespDto getOrderDetailsResult(Order findOrder,
                                                             List<DeliveryDetailsRespDto> deliveryRespDtos,
                                                             List<OrderProductDetailsRespDto> orderDetailsRespDtos) {
        return new OrderDetailsRespDto(findOrder, deliveryRespDtos, orderDetailsRespDtos);
    }

}
