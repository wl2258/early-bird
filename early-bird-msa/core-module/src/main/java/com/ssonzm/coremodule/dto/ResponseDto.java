package com.ssonzm.coremodule.dto;

import lombok.Data;

@Data
public class ResponseDto<T>{
    private int code;
    private String msg;
    private T body;
}