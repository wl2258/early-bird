package com.ssonzm.orderservice.config.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
import com.ssonzm.coremodule.dto.property.KafkaProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;

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

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations(deserializer), new StringDeserializer(), deserializer);
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

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations(deserializer), new StringDeserializer(), deserializer);
    }

    private Map<String, Object> consumerConfigurations(JsonDeserializer<?> deserializer) {
        Map<String, Object> consumerConfigurations = new HashMap<>();
        consumerConfigurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getHost());
        consumerConfigurations.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getOrder_group_id());
        consumerConfigurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfigurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
        consumerConfigurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return consumerConfigurations;
    }
}
