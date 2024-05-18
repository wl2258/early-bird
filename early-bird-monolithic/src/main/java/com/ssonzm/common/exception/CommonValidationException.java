package com.ssonzm.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CommonValidationException extends RuntimeException{

    private Map<String, String> errorMap;

    public CommonValidationException(String message, Map<String, String>errorMap) {
        super(message);
        this.errorMap = errorMap;
    }
}

