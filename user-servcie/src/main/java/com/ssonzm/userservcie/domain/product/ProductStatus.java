package com.ssonzm.userservcie.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    IN_STOCK("재고 있음"), SOLD_OUT("품절");
    private String value;
}
