package com.ssonzm.productservice.domain.order_product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    CREATED("신규 주문"), CANCELED("주문 취소");
    private String value;

}
