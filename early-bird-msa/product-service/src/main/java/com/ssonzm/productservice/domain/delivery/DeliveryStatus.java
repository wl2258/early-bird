package com.ssonzm.productservice.domain.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    READY_FOR_SHIPMENT("배송 준비 중"), SHIPPED("배송 중"),
    DELIVERED("배송 완료"), CANCELED("배송 취소");
    private String value;

}
