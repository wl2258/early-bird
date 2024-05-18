package com.ssonzm.domain.payment;

import com.ssonzm.domain.common.BaseEntity;
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

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    @Builder
    public Payment(Long id, Long userId, Long orderId, PaymentStatus status) {
        this.id = id;
        this.userId = userId;
        this.orderId = orderId;
        this.status = status;
    }
}
