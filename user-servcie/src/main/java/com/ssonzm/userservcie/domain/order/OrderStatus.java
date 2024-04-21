package com.ssonzm.userservcie.domain.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    CREATED("신규주문"), CANCELED("주문취소");
    private String value;

}
