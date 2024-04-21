package com.ssonzm.userservcie.domain.order_product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "order_products")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Column(name = "ordered_product_quantity", nullable = false)
    private int quantity;

    @Column(name = "ordered_product_price", nullable = false)
    private int price;

    private LocalDateTime expectedDeliveryDate;

    @Builder
    public OrderProduct(Long id, Long orderId, Long productId, int quantity, int price,
                        LocalDateTime expectedDeliveryDate) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void updateExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
}
