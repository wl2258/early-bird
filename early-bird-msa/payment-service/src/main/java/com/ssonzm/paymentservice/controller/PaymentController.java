package com.ssonzm.paymentservice.controller;

import com.ssonzm.paymentservice.service.payment.PaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController( PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}