package com.ssonzm.paymentservice.service.kafka;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.dto.payment.kafka.PaymentResponseDto.PaymentKafkaRollbackRespDto;
import com.ssonzm.paymentservice.domain.payment.PaymentStatus;
import com.ssonzm.paymentservice.service.payment.PaymentInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.ssonzm.coremodule.vo.KafkaVo.*;
import static com.ssonzm.paymentservice.dto.PaymentResponseDto.PaymentSaveRespDto;

@Slf4j
@Service
public class KafkaReceiver {

    private final KafkaSender kafkaSender;
    private final PaymentInternalService paymentService;

    public KafkaReceiver(KafkaSender kafkaSender, PaymentInternalService paymentService) {
        this.kafkaSender = kafkaSender;
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = KAFKA_PAYMENT_TOPIC, containerFactory = "paymentKafkaContainerFactory")
    public void receivePaymentSaveMessage(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        log.debug("[Payment Consumer]: 결제 엔티티 생성");

        PaymentSaveRespDto paymentSaveRespDto = new PaymentSaveRespDto();
        try {
            paymentSaveRespDto = paymentService.savePayment(paymentSaveKafkaReqDto);
        } catch (Exception e) {
            handlingException(paymentSaveKafkaReqDto, paymentSaveRespDto.getPaymentId());
        }

        PaymentStatus paymentStatus = paymentSaveRespDto.getPaymentStatus();
        if (isFailedSaveEntity(paymentStatus)) {
            sendToTopics(paymentSaveKafkaReqDto);
        }
    }

    private static boolean isFailedSaveEntity(PaymentStatus paymentStatus) {
        return paymentStatus != null && !paymentStatus.equals(PaymentStatus.SUCCESS);
    }

    private void handlingException(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto, Long paymentId) {
        log.error("[Payment Consumer]: Rollback");

        sendToTopics(paymentSaveKafkaReqDto);

        // 결제 상태 변경 (결제 실패)
        if (paymentId != null) paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
    }

    private void sendToTopics(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        sendMessageToKafkaPaymentRollbackTopic(KAFKA_PAYMENT_ROLLBACK_TOPIC, paymentSaveKafkaReqDto); // to order
        sendMessageToKafkaPaymentRollbackTopic(KAFKA_PAYMENT_TO_PRODUCT_TOPIC, paymentSaveKafkaReqDto); // to product
    }

    private void sendMessageToKafkaPaymentRollbackTopic(String topic, PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        kafkaSender.sendMessage(topic,
                new PaymentKafkaRollbackRespDto(
                        paymentSaveKafkaReqDto.getOrderId(),
                        paymentSaveKafkaReqDto.getProductId(),
                        paymentSaveKafkaReqDto.getQuantity())
        );
    }
}
