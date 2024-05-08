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
        return Payment.builder()
//                .status(PaymentStatus.NOT_PAY)
                .userId(paymentSaveKafkaReqDto.getUserId())
                .orderId(paymentSaveKafkaReqDto.getOrderId())
                .amount(paymentSaveKafkaReqDto.getAmount())
                .build();
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
