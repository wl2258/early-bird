package com.ssonzm.userservcie.domain.delivery;

import com.ssonzm.userservcie.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "deliveries")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private Long id;

    @Column(nullable = false)
    private Long orderProductId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus status;

    @Builder
    public Delivery(Long id, Long orderProductId, DeliveryStatus status) {
        this.id = id;
        this.orderProductId = orderProductId;
        this.status = status;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.status = deliveryStatus;
    }
}
