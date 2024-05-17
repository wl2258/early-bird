package com.ssonzm.paymentservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.paymentservice.service.payment.PaymentInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.vo.KafkaVo.KAFKA_PAYMENT_TOPIC;

@Slf4j
@Service
public class KafkaReceiver {

    private final PaymentInternalService paymentService;

    public KafkaReceiver(PaymentInternalService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = KAFKA_PAYMENT_TOPIC, containerFactory = "paymentKafkaContainerFactory")
    public void receivePaymentSaveMessage(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        log.debug("[Payment Consumer]: 결제 엔티티 생성");

        paymentService.savePayment(paymentSaveKafkaReqDto);
    }

}
