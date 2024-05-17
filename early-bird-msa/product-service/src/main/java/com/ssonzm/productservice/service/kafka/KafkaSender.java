package com.ssonzm.productservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, OrderSaveKafkaReqDto> productKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, OrderSaveKafkaReqDto> productKafkaTemplate) {
        this.productKafkaTemplate = productKafkaTemplate;
    }

    public void sendMessage(String topic, OrderSaveKafkaReqDto message) {
        productKafkaTemplate.send(topic, message);
    }
}
