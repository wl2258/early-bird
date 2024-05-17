package com.ssonzm.productservice.service.event;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {
    private Long userId;
    private OrderProductUpdateReqDto orderProductUpdateReqDto;
    public ProductEvent(Object source, Long userId,
                        OrderProductUpdateReqDto orderProductUpdateReqDto) {
        super(source);
        this.userId = userId;
        this.orderProductUpdateReqDto = orderProductUpdateReqDto;
    }

    public Long getUserId() {
        return userId;
    }


    public OrderProductUpdateReqDto getOrderProductUpdateReqDto() {
        return orderProductUpdateReqDto;
    }
}
