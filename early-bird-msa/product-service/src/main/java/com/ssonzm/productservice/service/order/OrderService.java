package com.ssonzm.productservice.service.order;

import com.ssonzm.productservice.domain.order.Order;
import com.ssonzm.productservice.dto.order.OrderRequestDto.OrderSaveReqDto;

import java.util.List;

import static com.ssonzm.productservice.dto.order.OrderResponseDto.*;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);

    Order findOrderByIdOrElseThrow(Long orderId);

    OrderDetailsRespDto getOrderDetail(Long orderId);
}
