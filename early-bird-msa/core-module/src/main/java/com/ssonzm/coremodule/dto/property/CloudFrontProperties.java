package com.ssonzm.coremodule.dto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cloud.aws.cloudfront")
public class CloudFrontProperties {
    private String domain;
    private String keyPairId;
}
