package com.ssonzm.userservcie.domain.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeliveryStatus {

    READY_FOR_SHIPMENT("배송준비중"), SHIPPED("배송중"), DELIVERED("배송완료");
    private String value;

}
