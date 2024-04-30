package com.ssonzm.orderservice.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.coremodule.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.coremodule.dto.product.ProductResponseDto.ProductDetailsFeignClientRespDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryRepository;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order.OrderRepository;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderProductRepository;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.service.aws_sqs.AmazonSqsSender;
import com.ssonzm.orderservice.service.client.ProductServiceClient;
import com.ssonzm.orderservice.service.delivery.DeliveryService;
import com.ssonzm.orderservice.service.order_product.OrderProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.ssonzm.coremodule.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import static com.ssonzm.coremodule.dto.order.OrderResponseDto.OrderDetailsRespDto;
import static com.ssonzm.coremodule.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;
import static com.ssonzm.coremodule.dto.order_product.OrderProjectRequestDto.OrderProductUpdateReqDto;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final AmazonSqsSender sqsSender;
    private final DeliveryService deliveryService;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderProductService orderProductService;
    private final ProductServiceClient productServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(AmazonSqsSender sqsSender, DeliveryService deliveryService, OrderRepository orderRepository,
                            DeliveryRepository deliveryRepository, OrderProductService orderProductService,
                            ProductServiceClient productServiceClient, CircuitBreakerFactory circuitBreakerFactory,
                            OrderProductRepository orderProductRepository) {
        this.sqsSender = sqsSender;
        this.deliveryService = deliveryService;
        this.orderRepository = orderRepository;
        this.deliveryRepository = deliveryRepository;
        this.orderProductService = orderProductService;
        this.productServiceClient = productServiceClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
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

        updateQuantity(orderProductList);

        return savedOrder.getId();
    }

    private void updateQuantity(List<OrderProduct> orderProductList) {
        List<OrderProductUpdateReqDto> orderProductUpdateReqDtoList = orderProductList.stream()
                .map(op -> new OrderProductUpdateReqDto(op.getProductId(), -op.getQuantity()))
                .toList();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(orderProductUpdateReqDtoList);
            sqsSender.sendMessage(message);
        } catch (JsonProcessingException e) {
            throw new CommonBadRequestException("failSqsSender");
        }
    }

    private Order createOrder(Long userId) {
        return orderRepository.save(Order.builder().userId(userId).build());
    }

    private List<OrderProduct> createOrderProducts(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList, Order savedOrder) {

        List<Long> productIds = orderSaveReqDtoList.stream()
                .map(OrderSaveReqDto::getProductId)
                .toList();

        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("product-circuit-breaker");

        List<ProductDetailsFeignClientRespDto> productDetailsList = circuitbreaker
                .run(() -> productServiceClient.getProductDetailsByIds(productIds).getBody().getBody(),
                        throwable -> {
                            throw new CommonBadRequestException("tryAgain", throwable);
                        });
        
        return orderSaveReqDtoList.stream()
                .map(orderSaveReqDto -> createOrderProduct(userId, orderSaveReqDto, productDetailsList, savedOrder))
                .collect(Collectors.toList());
    }

    private OrderProduct createOrderProduct(Long userId, OrderSaveReqDto orderSaveReqDto,
                                            List<ProductDetailsFeignClientRespDto> productDetailsList,
                                            Order savedOrder) {
        ProductDetailsFeignClientRespDto productDetails = productDetailsList.stream()
                .filter(p -> p.getId().equals(orderSaveReqDto.getProductId()))
                .findFirst()
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));

        int quantity = orderSaveReqDto.getQuantity();
        return OrderProduct.builder()
                .order(savedOrder)
                .userId(userId)
                .productId(productDetails.getId())
                .quantity(quantity)
                .price(quantity * productDetails.getPrice())
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

    private OrderDetailsRespDto getOrderDetailsResult(Order findOrder,
                                                      List<DeliveryDetailsRespDto> deliveryRespDtos,
                                                      List<OrderProductDetailsRespDto> orderDetailsRespDtos) {
        return new OrderDetailsRespDto(findOrder.getTotalPrice(), findOrder.getCreatedDate(),
                deliveryRespDtos, orderDetailsRespDtos);
    }
}
