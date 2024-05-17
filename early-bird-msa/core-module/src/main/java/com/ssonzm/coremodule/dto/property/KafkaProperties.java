package com.ssonzm.coremodule.dto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {
    private String host;
    private String order_group_id;
    private String product_group_id;
    private String payment_group_id;
}
