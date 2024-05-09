package com.ssonzm.productservice.service.event;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.productservice.domain.product.Product;
import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {
    private Long userId;
    private Product product;
    private OrderProductUpdateReqDto orderProductUpdateReqDto;
    public ProductEvent(Object source, Long userId, Product product,
                        OrderProductUpdateReqDto orderProductUpdateReqDto) {
        super(source);
        this.userId = userId;
        this.product = product;
        this.orderProductUpdateReqDto = orderProductUpdateReqDto;
    }

    public Long getUserId() {
        return userId;
    }

    public Product getProduct() {
        return product;
    }

    public OrderProductUpdateReqDto getOrderProductUpdateReqDto() {
        return orderProductUpdateReqDto;
    }
}
