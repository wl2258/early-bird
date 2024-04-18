package com.ssonzm.userservcie.common.exception;

public class CommonRuntimeException extends RuntimeException {

    private String[] detailMessage;

    public CommonRuntimeException() {
        super();
    }

    public CommonRuntimeException(String message) {
        super(message);
    }

    public CommonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonRuntimeException(String message, String[] detailMessage) {
        super(message);
        this.detailMessage = detailMessage;
    }

}
