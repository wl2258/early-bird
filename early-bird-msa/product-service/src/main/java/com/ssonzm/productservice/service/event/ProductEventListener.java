package com.ssonzm.productservice.service.event;

import com.ssonzm.coremodule.dto.order_product.OrderProductRequestDto.OrderProductUpdateReqDto;
import com.ssonzm.coremodule.dto.product.kafka.ProductRequestDto.OrderSaveKafkaReqDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.productservice.service.kafka.KafkaSender;
import com.ssonzm.productservice.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ProductEventListener {
    private final KafkaSender kafkaSender;
    private final ProductService productService;

    public ProductEventListener(KafkaSender kafkaSender, ProductService productService) {
        this.kafkaSender = kafkaSender;
        this.productService = productService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProductSuccess(ProductEvent event) {
        Long userId = event.getUserId();
        OrderProductUpdateReqDto orderProductUpdateReqDto = event.getOrderProductUpdateReqDto();

        Long productId = event.getOrderProductUpdateReqDto().getProductId();
        try {
            log.debug("[Product producer(productId = {})] 주문 엔티티 생성 이벤트 발행",
                    productId);
            kafkaSender.sendMessage(KafkaVo.KAFKA_ORDER_TOPIC,
                    createOrderSaveDto(userId, orderProductUpdateReqDto));
        } catch (Exception e) {
            log.error("[Product producer(productId = {})] 카프카 이벤트 발행 중 에러 발생",
                    productId);
            productService.increaseQuantityByLua(productId, orderProductUpdateReqDto.getQuantity());
        }
    }

    private OrderSaveKafkaReqDto createOrderSaveDto(Long userId,
                                                    OrderProductUpdateReqDto orderProductUpdateReqDto) {
        return new OrderSaveKafkaReqDto(
                userId,
                orderProductUpdateReqDto.getQuantity(),
                orderProductUpdateReqDto.getProductId(),
                orderProductUpdateReqDto.getPrice());
    }
}
