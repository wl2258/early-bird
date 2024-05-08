package com.ssonzm.paymentservice.config.kafka;

import com.google.common.collect.ImmutableMap;
import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
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
    public Map<String, Object> producerConfigurations() {
        return ImmutableMap.<String, Object>builder()
                .put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost())
                .put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .put(ACKS_CONFIG, "all") // 높은 신뢰성 설정
                .build();
    }

    @Bean
    public ProducerFactory<String, PaymentKafkaRollbackRespDto> paymentFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    @Bean
    public KafkaTemplate<String, PaymentKafkaRollbackRespDto> paymentKafkaTemplate() {
        return new KafkaTemplate<>(paymentFactory());
    }
}
