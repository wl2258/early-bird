package com.ssonzm.userservcie.service.order;

import com.ssonzm.userservcie.domain.order.Order;
import com.ssonzm.userservcie.dto.order.OrderRequestDto.OrderSaveReqDto;

import java.util.List;

import static com.ssonzm.userservcie.dto.order.OrderResponseDto.*;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);

    Order findOrderByIdOrElseThrow(Long orderId);

    OrderDetailsRespDto getOrderDetail(Long orderId);
}
