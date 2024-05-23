package com.ssonzm.coremodule.dto.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "cloud.aws")
public class S3Properties {
    private String region;
    private S3 s3;

    @Data
    public static class S3 {
        private String bucket;
        private Credentials credentials;
    }

    @Data
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
}
