package com.ssonzm.orderservice.config.kafka;

import com.google.common.collect.ImmutableMap;
import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
import com.ssonzm.coremodule.dto.property.KafkaProperties;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
public class ConsumerConfiguration {
    private final KafkaProperties kafkaProperties;

    public ConsumerConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderSaveKafkaReqDto> orderKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderSaveKafkaReqDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderSaveKafkaReqDto> orderConsumerFactory() {
        JsonDeserializer<OrderSaveKafkaReqDto> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost())
                        .put(GROUP_ID_CONFIG, kafkaProperties.getGroup_id())
                        .put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentKafkaRollbackRespDto> paymentRollbackKafkaContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentKafkaRollbackRespDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentRollbackConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentKafkaRollbackRespDto> paymentRollbackConsumerFactory() {
        JsonDeserializer<PaymentKafkaRollbackRespDto> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost())
                        .put(GROUP_ID_CONFIG, kafkaProperties.getGroup_id())
                        .put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }
}
