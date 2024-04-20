package com.ssonzm.userservcie.domain.product;

import com.ssonzm.userservcie.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private User user;

    @Column(name = "product_name", nullable = false)
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

    @Builder
    public Product(Long id, User user, String name, ProductCategory category,
                   String description, ProductStatus status, int quantity, int price) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.category = category;
        this.description = description;
        this.status = status;
        this.quantity = quantity;
        this.price = price;
    }
}
