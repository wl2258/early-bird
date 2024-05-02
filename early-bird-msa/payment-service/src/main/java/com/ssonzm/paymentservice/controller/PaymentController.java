package com.ssonzm.paymentservice.controller;

import com.ssonzm.paymentservice.service.payment.PaymentService;

public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}