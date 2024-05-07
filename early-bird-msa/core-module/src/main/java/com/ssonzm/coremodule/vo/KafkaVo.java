package com.ssonzm.coremodule.vo;

public interface KafkaVo {
    String KAFKA_PRODUCT_TOPIC = "early-bird-product"; // product -> order
    String KAFKA_PRODUCT_ROLLBACK_TOPIC = "early-bird-product-rollback"; // order -> product

}
