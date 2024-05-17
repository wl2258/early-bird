package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, ProductKafkaRollbackRespDto> orderKafkaTemplate;
    private final KafkaTemplate<String, PaymentSaveKafkaReqDto> paymentKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, ProductKafkaRollbackRespDto> orderKafkaTemplate,
                       KafkaTemplate<String, PaymentSaveKafkaReqDto> paymentKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }

    public void sendMessage(String topic, ProductKafkaRollbackRespDto message) {
        orderKafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, PaymentSaveKafkaReqDto message) {
        paymentKafkaTemplate.send(topic, message);
    }
}