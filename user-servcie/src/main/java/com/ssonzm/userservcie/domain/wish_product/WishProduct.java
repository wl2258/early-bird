package com.ssonzm.userservcie.domain.wish_product;

import com.ssonzm.userservcie.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "wish_products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_product_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long productId;

    @Column(name = "wish_product_quantity", nullable = false)
    private int quantity;

    @Column(name = "wish_product_price", nullable = false)
    private int price;

    @Builder
    public WishProduct(Long id, Long userId, Long productId, int quantity, int price) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void addPrice(int price) {
        this.price += price;
    }
}
