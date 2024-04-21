package com.ssonzm.userservcie.service.order;

import com.ssonzm.userservcie.dto.order.OrderRequestDto.OrderSaveReqDto;

import java.util.List;

public interface OrderService {
    Long saveOrder(Long userId, List<OrderSaveReqDto> orderSaveReqDtoList);
}
