package com.ssonzm.orderservice.controller.order_product;

import com.ssonzm.coremodule.dto.ResponseDto;
import com.ssonzm.coremodule.util.ResponseUtil;
import com.ssonzm.orderservice.service.order_product.OrderProductService;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderProductController {
    private final MessageSource messageSource;
    private final OrderProductService orderProductService;

    public OrderProductController(MessageSource messageSource, OrderProductService orderProductService) {
        this.messageSource = messageSource;
        this.orderProductService = orderProductService;
    }

    @PostMapping("/authz/orders/{orderProductId}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderProductId) {

        orderProductService.cancelOrderProduct(orderProductId);

        ResponseDto<?> responseDto = ResponseUtil.setResponseDto(messageSource, true);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
