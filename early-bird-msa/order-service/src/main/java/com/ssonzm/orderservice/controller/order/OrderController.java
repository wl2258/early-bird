package com.ssonzm.orderservice.controller.order;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.orderservice.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.orderservice.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ssonzm.orderservice.dto.order.OrderResponseDto.OrderDetailsRespDto;

@RestController
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final MessageSource messageSource;

    public OrderController(OrderService orderService, MessageSource messageSource) {
        this.orderService = orderService;
        this.messageSource = messageSource;
    }

    @PostMapping("/authz/orders")
    public ResponseEntity<?> saveOrder(@RequestBody @Valid List<OrderSaveReqDto> orderSaveReqDtoList,
                                       BindingResult bindingResult,
                                       @RequestHeader("x_user_id") Long userId) {

        Long orderId = orderService.saveOrder(userId, orderSaveReqDtoList);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/authz/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable Long orderId) {

        OrderDetailsRespDto orderDetails = orderService.getOrderDetail(orderId);

        ResponseDto<OrderDetailsRespDto> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(orderDetails);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
