package com.ssonzm.productservice.service.aws_sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.ProductUpdateAfterOrderReqDto;
import com.ssonzm.productservice.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class AmazonSqsListener {
    private final Environment env;
    private final ObjectMapper objectMapper;
    private final ProductService productService;

    public AmazonSqsListener(Environment env, ObjectMapper objectMapper, ProductService productService) {
        this.env = env;
        this.objectMapper = objectMapper;
        this.productService = productService;
    }

//    @SqsListener(queueNames="${cloud.aws.sqs.queue-name}")
    // TODO sqs listener exception retry
    public void messageListener(Message message) {
        try {
            String body = message.body();
            String messageGroupId = message.attributesAsStrings().get("MessageGroupId");

            if (messageGroupId.equals(env.getProperty("sqs.product.group-id"))) {
                List<ProductUpdateAfterOrderReqDto> orderProductUpdateList =
                        Arrays.asList(objectMapper.readValue(body, ProductUpdateAfterOrderReqDto[].class));

                productService.updateProductQuantity(orderProductUpdateList);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to process message: {}", e.getMessage(), e);
        }
    }
}
