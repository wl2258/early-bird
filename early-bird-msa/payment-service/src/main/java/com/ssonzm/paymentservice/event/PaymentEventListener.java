package com.ssonzm.paymentservice.event;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.paymentservice.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.createPaymentKafkaRollbackRespDto;
import static com.ssonzm.coremodule.vo.KafkaVo.KAFKA_PAYMENT_ROLLBACK_TOPIC;
import static com.ssonzm.coremodule.vo.KafkaVo.KAFKA_PAYMENT_TO_PRODUCT_TOPIC;

@Slf4j
@Component
public class PaymentEventListener {
    private final KafkaSender kafkaSender;

    public PaymentEventListener(KafkaSender kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION) // commit or rollback
    public void publishPaymentEvent(PaymentEvent event) {
        log.debug("[Payment producer] 결제 실패 롤백 이벤트 발행");

        PaymentSaveKafkaReqDto paymentSaveKafkaReqDto = event.getPaymentSaveKafkaReqDto();
        kafkaSender.sendMessage(KAFKA_PAYMENT_ROLLBACK_TOPIC,
                createPaymentKafkaRollbackRespDto(paymentSaveKafkaReqDto));
        kafkaSender.sendMessage(KAFKA_PAYMENT_TO_PRODUCT_TOPIC,
                createPaymentKafkaRollbackRespDto(paymentSaveKafkaReqDto));
    }
}