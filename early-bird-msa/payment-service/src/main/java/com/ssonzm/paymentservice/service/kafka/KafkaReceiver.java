package com.ssonzm.paymentservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
import com.ssonzm.coremodule.vo.KafkaVo;
import com.ssonzm.paymentservice.domain.payment.PaymentStatus;
import com.ssonzm.paymentservice.service.payment.PaymentInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaReceiver {

    private final KafkaSender kafkaSender;
    private final PaymentInternalService paymentService;

    public KafkaReceiver(KafkaSender kafkaSender, PaymentInternalService paymentService) {
        this.kafkaSender = kafkaSender;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = KafkaVo.KAFKA_PAYMENT_TOPIC, containerFactory = "paymentKafkaContainerFactory")
    public void receivePaymentSaveMessage(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        log.debug("[Payment Consumer]: 결제 엔티티 생성");

        Long paymentId = null;
        try {
            paymentId = paymentService.savePayment(paymentSaveKafkaReqDto);
        } catch (Exception e) {
            handlingException(paymentSaveKafkaReqDto, paymentId);
        }
    }

    private void handlingException(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto, Long paymentId) {
        log.error("[Payment Consumer]: Rollback");

        sendMessageToKafkaPaymentRollbackTopic(paymentSaveKafkaReqDto);

        // 결제 상태 변경 (결제 실패)
        paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
    }

    private void sendMessageToKafkaPaymentRollbackTopic(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        kafkaSender.sendMessage(KafkaVo.KAFKA_PAYMENT_ROLLBACK_TOPIC,
                new PaymentKafkaRollbackRespDto(paymentSaveKafkaReqDto.getOrderId()));
    }
}
