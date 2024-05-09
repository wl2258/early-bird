package com.ssonzm.productservice.service.event;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.domain.product.Product;
import com.ssonzm.productservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProductEventListener {
    private final KafkaSender kafkaSender;

    public ProductEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductSuccess(ProductEvent event) {
        Long userId = event.getUserId();
        Product product = event.getProduct();
        OrderProductUpdateReqDto orderProductUpdateReqDto = event.getOrderProductUpdateReqDto();

        log.debug("[Product producer(productId = {})] 주문 엔티티 생성 이벤트 발행", product.getId());
        kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_TOPIC,
                createOrderSaveDto(userId, product,orderProductUpdateReqDto));
    }

    private ProductRequestDto.OrderSaveKafkaReqDto createOrderSaveDto(Long userId, Product product,
                                                                      OrderProductUpdateReqDto orderProductUpdateReqDto) {
        int quantity = orderProductUpdateReqDto.getQuantity();
        return new ProductRequestDto.OrderSaveKafkaReqDto(userId, quantity, product.getId(), product.getPrice());
    }
}
