package com.ssonzm.paymentservice.service.payment;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.paymentservice.domain.payment.Payment;
import com.ssonzm.paymentservice.domain.payment.PaymentRepository;
import com.ssonzm.paymentservice.domain.payment.PaymentStatus;
import com.ssonzm.paymentservice.event.PaymentEvent;
import com.ssonzm.paymentservice.event.PaymentEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class PaymentInternalService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventListener paymentEventListener;

    public PaymentInternalService(PaymentRepository paymentRepository, PaymentEventListener paymentEventListener) {
        this.paymentRepository = paymentRepository;
        this.paymentEventListener = paymentEventListener;
    }


    @Transactional
    public void savePayment(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        Payment payment = paymentRepository.save(createPayment(paymentSaveKafkaReqDto));

        if (isFailedSaveEntity(payment.getStatus())) {
            log.debug("[Payment service] 결제 중 고객 이탈");
            paymentEventListener.publishPaymentEvent(new PaymentEvent(this, paymentSaveKafkaReqDto));
        }
    }

    private static boolean isFailedSaveEntity(PaymentStatus paymentStatus) {
        return paymentStatus != null && !paymentStatus.equals(PaymentStatus.SUCCESS);
    }

    private Payment createPayment(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        PaymentStatus status = PaymentStatus.SUCCESS;
        if (isCanceled()) {
            status = PaymentStatus.CANCELLED;
        } else if (isFailed()) {
            status = PaymentStatus.FAILED;
        }
        return Payment.builder()
                .status(status)
                .userId(paymentSaveKafkaReqDto.getUserId())
                .orderId(paymentSaveKafkaReqDto.getOrderId())
                .amount(paymentSaveKafkaReqDto.getQuantity() * paymentSaveKafkaReqDto.getPrice())
                .build();
    }
    private boolean isCanceled() {
        return Math.random() < 0.2;
    }

    private boolean isFailed() {
        return Math.random() < 0.2;
    }

    @Transactional
    public void updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus) {
        Payment findPayment = findPaymentByIdOrElseThrow(paymentId);
        findPayment.updatePaymentStatus(paymentStatus);
    }

    public Payment findPaymentByIdOrElseThrow(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new CommonBadRequestException("notFoundData"));
    }
}
