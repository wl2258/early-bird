package com.ssonzm.userservcie.service.order;

import com.ssonzm.userservcie.common.exception.CommonBadRequestException;
import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.domain.order.OrderRepository;
import com.ssonzm.userservcie.domain.order.OrderStatus;
import com.ssonzm.userservcie.domain.order_product.OrderProduct;
import com.ssonzm.userservcie.domain.order_product.OrderProductRepository;
import com.ssonzm.userservcie.domain.product.Product;
import com.ssonzm.userservcie.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.userservcie.service.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    public OrderServiceImpl(ProductService productService, OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @Override
    @Transactional
    public Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList) {
        Order savedOrder = createOrder(userId);
        List<OrderProduct> orderProducts = createOrderProducts(orderSaveReqDtoList, savedOrder);
        int totalPrice = calculateTotalPrice(orderProducts);

        orderProductRepository.saveAll(orderProducts);
        savedOrder.updateTotalPrice(totalPrice);

        return savedOrder.getId();
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order findOrder = findOrderByIdOrElseThrow(orderId);
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
