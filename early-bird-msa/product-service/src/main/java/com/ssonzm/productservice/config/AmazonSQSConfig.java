package com.ssonzm.productservice.config;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AmazonSQSConfig {
    private final Environment env;

    public AmazonSQSConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {

        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return env.getProperty("cloud.aws.credentials.access-key");
                    }

                    @Override
                    public String secretAccessKey() {
                        return env.getProperty("cloud.aws.credentials.secret-key");
                    }
                })
                .region(Region.of(env.getProperty("cloud.aws.region.static")))
                .build();
    }

    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }
}
