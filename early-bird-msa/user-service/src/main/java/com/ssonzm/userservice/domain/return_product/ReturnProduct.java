package com.ssonzm.userservice.domain.return_product;

import com.ssonzm.coremodule.domain.BaseEntity;
import com.ssonzm.userservice.domain.order_product.OrderProduct;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "returns")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReturnProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id", nullable = false)
    private OrderProduct orderProduct;

    @Column(name = "return_reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "return_status", nullable = false)
    private ReturnStatus status;

    @Builder
    public ReturnProduct(Long id, Long userId, OrderProduct orderProduct, String reason, ReturnStatus status) {
        this.id = id;
        this.userId = userId;
        this.orderProduct = orderProduct;
        this.reason = reason;
        this.status = status;
    }

    public void updateStatus(ReturnStatus returnStatus) {
        this.status = returnStatus;
    }
}