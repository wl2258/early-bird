package com.ssonzm.paymentservice.event;

import org.springframework.context.ApplicationEvent;

import static com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;

public class PaymentEvent extends ApplicationEvent {

    private PaymentSaveKafkaReqDto paymentSaveKafkaReqDto;
    public PaymentEvent(Object source, PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        super(source);
        this.paymentSaveKafkaReqDto = paymentSaveKafkaReqDto;
    }

    public PaymentSaveKafkaReqDto getPaymentSaveKafkaReqDto() {
        return paymentSaveKafkaReqDto;
    }
}