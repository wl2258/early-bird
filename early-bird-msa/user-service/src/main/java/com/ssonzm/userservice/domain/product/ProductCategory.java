package com.ssonzm.userservice.domain.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductCategory {

    FASHION("패션"), BEAUTY("뷰티");
    private String value;

}
