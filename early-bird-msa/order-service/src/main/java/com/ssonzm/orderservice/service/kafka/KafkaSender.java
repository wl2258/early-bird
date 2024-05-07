package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, ProductKafkaRollbackRespDto> productKafkaTemplate;
    private final KafkaTemplate<String, PaymentSaveKafkaReqDto> paymentKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, ProductKafkaRollbackRespDto> productKafkaTemplate,
                       KafkaTemplate<String, PaymentSaveKafkaReqDto> paymentKafkaTemplate) {
        this.productKafkaTemplate = productKafkaTemplate;
        this.paymentKafkaTemplate = paymentKafkaTemplate;
    }

    public void sendMessage(String topic, ProductKafkaRollbackRespDto message) {
        productKafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, PaymentSaveKafkaReqDto message) {
        paymentKafkaTemplate.send(topic, message);
    }
}