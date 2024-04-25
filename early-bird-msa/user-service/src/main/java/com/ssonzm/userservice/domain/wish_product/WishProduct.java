package com.ssonzm.userservice.domain.wish_product;

import com.ssonzm.coremodule.domain.BaseEntity;
import com.ssonzm.userservice.domain.product.Product;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "wish_product_quantity", nullable = false)
    private int quantity;

    @Column(name = "wish_product_price", nullable = false)
    private int price;

    @Builder
    public WishProduct(Long id, Long userId, Product product, int quantity, int price) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public void updateQuantityAndPrice(int quantity, int price) {
        this.quantity = quantity;
        this.price = price;
    }
}
