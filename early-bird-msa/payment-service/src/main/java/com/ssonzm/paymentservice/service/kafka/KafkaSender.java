package com.ssonzm.paymentservice.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, PaymentKafkaRollbackRespDto> paymentRollbackKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, PaymentKafkaRollbackRespDto> paymentRollbackKafkaTemplate) {
        this.paymentRollbackKafkaTemplate = paymentRollbackKafkaTemplate;
    }

    public void sendMessage(String topic, PaymentKafkaRollbackRespDto message) {
        paymentRollbackKafkaTemplate.send(topic, message);
    }
}