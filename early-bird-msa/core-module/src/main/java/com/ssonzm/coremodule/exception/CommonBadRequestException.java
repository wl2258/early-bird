package com.ssonzm.coremodule.exception;

public class CommonBadRequestException extends CommonRuntimeException {
    public CommonBadRequestException(String message) {
        super(message);
    }

    public CommonBadRequestException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
