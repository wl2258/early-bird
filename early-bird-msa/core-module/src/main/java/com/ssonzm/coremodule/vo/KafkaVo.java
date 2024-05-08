package com.ssonzm.coremodule.vo;

public interface KafkaVo {
    String KAFKA_ORDER_TOPIC = "early-bird-order"; // product -> order
    String KAFKA_ORDER_ROLLBACK_TOPIC = "early-bird-order-rollback"; // order -> product
    String KAFKA_PAYMENT_TOPIC = "early-bird-payment"; // order -> payment
    String KAFKA_PAYMENT_ROLLBACK_TOPIC = "early-bird-payment-rollback"; // payment -> order
}
