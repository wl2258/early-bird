package com.ssonzm.productservice.service.aws_sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.productservice.service.product.ProductService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.ssonzm.coremodule.dto.order_product.OrderProjectRequestDto.OrderProductUpdateReqDto;

@Slf4j
@Component
public class AmazonSqsListener {
    private final ProductService productService;

    public AmazonSqsListener(ProductService productService) {
        this.productService = productService;
    }

    @SqsListener(queueNames="${cloud.aws.sqs.queue-name}")
    public void messageListener(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<OrderProductUpdateReqDto> orderProductUpdateList = Arrays.asList(objectMapper.readValue(
                    message, OrderProductUpdateReqDto[].class));

            productService.updateProductQuantity(orderProductUpdateList);
        } catch (JsonProcessingException e) {
            throw new CommonBadRequestException("failSqsListener");
        }
    }
}
