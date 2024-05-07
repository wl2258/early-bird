package com.ssonzm.orderservice.config.kafka;

import com.google.common.collect.ImmutableMap;
import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductResponseDto.ProductKafkaRollbackRespDto;
import com.ssonzm.coremodule.dto.property.KafkaProperties;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@EnableKafka
@Configuration
public class ProducerConfiguration {
    private final KafkaProperties kafkaProperties;

    public ProducerConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ProducerFactory<String, ProductKafkaRollbackRespDto > productFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public Map<String, Object> producerConfigurations() {
        return ImmutableMap.<String, Object>builder()
                .put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost())
                .put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .put(ACKS_CONFIG, "all") // 높은 신뢰성 설정
                .build();
    }

    @Bean
    public KafkaTemplate<String, ProductKafkaRollbackRespDto> productKafkaTemplate() {
        return new KafkaTemplate<>(productFactory());
    }

    @Bean
    public ProducerFactory<String, PaymentSaveKafkaReqDto> paymentFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public KafkaTemplate<String, PaymentSaveKafkaReqDto> paymentKafkaTemplate() {
        return new KafkaTemplate<>(paymentFactory());
    }
}
