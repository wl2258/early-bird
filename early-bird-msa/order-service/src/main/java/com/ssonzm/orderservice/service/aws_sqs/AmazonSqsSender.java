package com.ssonzm.orderservice.service.aws_sqs;

import io.awspring.cloud.sqs.operations.SendResult;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AmazonSqsSender {
    private final Environment env;
    private final SqsTemplate sqsTemplate;

    public AmazonSqsSender(Environment env, SqsTemplate sqsTemplate) {
        this.env = env;
        this.sqsTemplate = sqsTemplate;
    }

    public SendResult<String> sendMessage(String message) {
        String queueName = env.getProperty("cloud.aws.sqs.queue-name");

        return sqsTemplate.send(to -> to
                .queue(queueName)
                .payload(message));
    }
}
