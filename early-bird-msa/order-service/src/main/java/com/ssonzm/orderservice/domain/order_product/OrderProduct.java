package com.ssonzm.orderservice.domain.order_product;

import com.ssonzm.coremodule.domain.BaseEntity;
import com.ssonzm.orderservice.domain.order.Order;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@Table(name = "order_products")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long productId;

    @Column(name = "ordered_product_quantity", nullable = false)
    private int quantity;

    @Column(name = "ordered_product_price", nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus status;

    @Builder
    public OrderProduct(Long id, Long userId, Order order, Long productId, int quantity, int price, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.order = order;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public void updateOrderStatus(OrderStatus status) {
        this.status = status;
    }
}
