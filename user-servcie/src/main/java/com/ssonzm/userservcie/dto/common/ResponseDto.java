package com.ssonzm.userservcie.dto.common;

import lombok.Data;

@Data
public class ResponseDto <T>{
    private int code;
    private String msg;
    private T body;
}