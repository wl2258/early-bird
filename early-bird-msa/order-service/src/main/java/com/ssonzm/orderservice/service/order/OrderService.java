package com.ssonzm.orderservice.service.order;

import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.coremodule.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;

import java.util.List;

import static com.ssonzm.coremodule.dto.order.OrderResponseDto.*;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);

    Order findOrderByIdOrElseThrow(Long orderId);

    OrderDetailsRespDto getOrderDetail(Long orderId);

    void updateOrderStatus(Long orderProductId, OrderStatus orderStatus);

    OrderProduct findOrderProductByIdOrElseThrow(Long orderProductId);
}
