package com.ssonzm.userservice.service.order;

import com.ssonzm.userservice.domain.order.Order;
import com.ssonzm.userservice.dto.order.OrderRequestDto.OrderSaveReqDto;

import java.util.List;

import static com.ssonzm.userservice.dto.order.OrderResponseDto.*;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);

    Order findOrderByIdOrElseThrow(Long orderId);

    OrderDetailsRespDto getOrderDetail(Long orderId);
}
