package com.ssonzm.userservcie.controller.order;

import com.ssonzm.userservcie.common.util.ResponseUtil;
import com.ssonzm.userservcie.config.security.PrincipalDetails;
import com.ssonzm.userservcie.dto.common.ResponseDto;
import com.ssonzm.userservcie.dto.order.OrderRequestDto.OrderSaveReqDto;
import com.ssonzm.userservcie.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {

        Long orderId = orderService.saveOrder(principalDetails.getUser().getId(), orderSaveReqDtoList);

        ResponseDto<Long> responseDto = ResponseUtil.setResponseDto(messageSource, true);
        responseDto.setBody(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/authz/orders/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
