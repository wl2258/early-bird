package com.ssonzm.paymentservice.domain.payment;

import com.ssonzm.coremodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "payments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Integer amount;

    @Builder
    public Payment(Long id, Long userId, Long orderId, PaymentStatus status, Integer amount) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
        this.amount = amount;
    }

    public void updatePaymentStatus(PaymentStatus status) {
        this.status = status;
    }
}
