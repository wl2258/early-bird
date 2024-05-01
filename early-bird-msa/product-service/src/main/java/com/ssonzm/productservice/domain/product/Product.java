package com.ssonzm.productservice.domain.product;

import com.ssonzm.coremodule.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "product_name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status", nullable = false)
    private ProductStatus status;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    private LocalDateTime reservationStartTime;

    @Builder
    public Product(Long id, Long userId, String name, ProductCategory category,
                   String description, ProductStatus status, int quantity, int price,
                   LocalDateTime reservationStartTime) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.category = category;
        this.description = description;
        this.status = status;
        this.quantity = quantity;
        this.price = price;
        this.reservationStartTime = reservationStartTime;
    }

    public void updateQuantity(int quantity) {
        this.quantity += quantity;
    }
}
