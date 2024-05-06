package com.ssonzm.productservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, OrderSaveKafkaReqDto> orderKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, OrderSaveKafkaReqDto> orderKafkaTemplate) {
        this.orderKafkaTemplate = orderKafkaTemplate;
    }

    public void sendMessage(String topic, OrderSaveKafkaReqDto message) {
        orderKafkaTemplate.send(topic, message);
    }
}
