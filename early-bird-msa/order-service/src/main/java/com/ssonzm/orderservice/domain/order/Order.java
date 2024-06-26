package com.ssonzm.orderservice.domain.order;

import com.ssonzm.coremodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private int totalPrice;

    @Builder
    public Order(Long id, Long userId, int totalPrice) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = totalPrice;
    }

    public void updateTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
