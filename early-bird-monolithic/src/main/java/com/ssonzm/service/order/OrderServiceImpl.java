package com.ssonzm.service.order;

import com.ssonzm.domain.order_product.OrderStatus;
import com.ssonzm.service.delivery.DeliveryService;
import com.ssonzm.service.order_product.OrderProductService;
import com.ssonzm.service.product.ProductService;
import com.ssonzm.common.exception.CommonBadRequestException;
import com.ssonzm.domain.delivery.Delivery;
import com.ssonzm.domain.delivery.DeliveryRepository;
import com.ssonzm.domain.delivery.DeliveryStatus;
import com.ssonzm.domain.order.Order;
import com.ssonzm.domain.order.OrderRepository;
import com.ssonzm.domain.order_product.OrderProduct;
import com.ssonzm.domain.order_product.OrderProductRepository;
import com.ssonzm.domain.product.Product;
import com.ssonzm.dto.order.OrderRequestDto.OrderSaveReqDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ssonzm.dto.delivery.DeliveryResponseDto.DeliveryDetailsRespDto;
import static com.ssonzm.dto.order.OrderResponseDto.OrderDetailsRespDto;
import static com.ssonzm.dto.order_product.OrderProductResponseDto.OrderProductDetailsRespDto;

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

        saveDeliveryList(orderProductList);
        return savedOrder.getId();
    }

    private Order createOrder(Long userId) {
        return orderRepository.save(Order.builder().userId(userId).build());
    }

    private List<OrderProduct> createOrderProducts(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList, Order savedOrder) {
        return orderSaveReqDtoList.stream()
                .map(dto -> createOrderProduct(userId, dto, productService.findProductByIdOrElseThrow(dto.getProductId()), savedOrder))
                .toList();
    }

    private OrderProduct createOrderProduct(Long userId, OrderSaveReqDto orderSaveReqDto, Product product, Order savedOrder) {
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
