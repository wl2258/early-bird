package com.ssonzm.userservcie.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentStatus {

    NOT_PAY("결제 전"), SUCCESS("결제 성공"), FAILED("결제 실패"), CANCELLED("결제 취소");
    private String value;

}
