package com.ssonzm.paymentservice.service.payment;

import com.ssonzm.coremodule.dto.payment.kafka.PaymentRequestDto.PaymentSaveKafkaReqDto;
import com.ssonzm.coremodule.exception.CommonBadRequestException;
import com.ssonzm.paymentservice.domain.payment.Payment;
import com.ssonzm.paymentservice.domain.payment.PaymentRepository;
import com.ssonzm.paymentservice.domain.payment.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PaymentInternalService {
    private final PaymentRepository paymentRepository;

    public PaymentInternalService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }


    @Transactional
    public Long savePayment(PaymentSaveKafkaReqDto paymentSaveKafkaReqDto) {
        Payment payment = paymentRepository.save(createPayment(paymentSaveKafkaReqDto));
        return payment.getId();
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
                .amount(paymentSaveKafkaReqDto.getAmount())
                .build();
    }
    private boolean isCanceled() {
        return Math.random() < 0.2;
    }

    private boolean isFailed() {
        return Math.random() < 0.2;
    }

    private boolean isPaymentFailure() {
        double randomValue = Math.random();
        if (randomValue < 0.4) {
            return true; // 40% 확률로 결제 실패
        } else {
            return false;
        }
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
