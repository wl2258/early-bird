package com.ssonzm.orderservice.service.kafka;

import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaSender {
    private final KafkaTemplate<String, ProductKafkaRollbackRespDto> productKafkaTemplate;

    public KafkaSender(KafkaTemplate<String, ProductKafkaRollbackRespDto> productKafkaTemplate) {
        this.productKafkaTemplate = productKafkaTemplate;
    }

    public void sendMessage(String topic, ProductKafkaRollbackRespDto message) {
        productKafkaTemplate.send(topic, message);
    }
}