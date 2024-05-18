package com.ssonzm.domain.return_product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReturnStatus {

    REQUESTED("반품 요청"), APPROVED("반품 승인"), DENIED("반품 거절");
    private String value;

}
