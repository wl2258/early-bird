package com.ssonzm.orderservice.service.order;

import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.coremodule.dto.order.OrderRequestDto.OrderSaveReqDto;

import java.util.List;

import static com.ssonzm.coremodule.dto.order.OrderResponseDto.*;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);

    Order findOrderByIdOrElseThrow(Long orderId);

    OrderDetailsRespDto getOrderDetail(Long orderId);
}
