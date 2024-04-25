package com.ssonzm.orderservice.common.util;

import com.ssonzm.orderservice.domain.delivery.Delivery;
import com.ssonzm.orderservice.domain.delivery.DeliveryStatus;
import com.ssonzm.orderservice.domain.order.Order;
import com.ssonzm.orderservice.domain.order_product.OrderProduct;
import com.ssonzm.orderservice.domain.order_product.OrderStatus;
import com.ssonzm.orderservice.domain.product.Product;
import com.ssonzm.orderservice.domain.product.ProductCategory;
import com.ssonzm.orderservice.domain.product.ProductStatus;
import com.ssonzm.orderservice.domain.user.User;
import com.ssonzm.orderservice.domain.user.UserRole;
import com.ssonzm.orderservice.domain.wish_product.WishProduct;

public class DummyUtil {
    public User newUser(String username, String email) {
        return User.builder()
                .username(username)
                .email(email)
                .address("서울특별시 종로구")
                .password("$2a$10$lfrIZHgfmHxPXkxqAoa9femfsvO5zoL2zXz.AgtKi3WrHe34pX3DW")
                .phoneNumber("010-1234-5678")
                .role(UserRole.USER)
                .build();
    }

    public Product newProduct(String name, ProductStatus status, Long userId) {
        return Product.builder()
                .name(name)
                .price(10000)
                .status(status)
                .userId(userId)
                .quantity(10000)
                .description("")
                .build();
    }

    public Product newMockProduct(Long id, String name, ProductStatus status, Long userId) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(10000)
                .status(status)
                .userId(userId)
                .quantity(10000)
                .description("test description")
                .category(ProductCategory.FASHION)
                .build();
    }


    public OrderProduct newOrderProduct(Long userId, Long productId, Order order, OrderStatus status) {
        return OrderProduct.builder()
                .order(order)
                .userId(userId)
                .status(status)
                .productId(productId)
                .quantity(1)
                .price(10000)
                .build();
    }

    public OrderProduct newMockOrderProduct(Long id, Long userId, Long productId, Order order, OrderStatus status) {
        return OrderProduct.builder()
                .order(order)
                .userId(userId)
                .status(status)
                .productId(productId)
                .quantity(1)
                .price(10000)
                .build();
    }

    public Order newOrder(Long userId) {
        return Order.builder()
                .userId(userId)
                .build();
    }

    public Order newMockOrder(Long id, Long userId) {
        return Order.builder()
                .id(id)
                .userId(userId)
                .build();
    }

    public Delivery newDelivery(Long orderProductId, DeliveryStatus status) {
        return Delivery.builder()
                .orderProductId(orderProductId)
                .status(status)
                .build();
    }

    public Delivery newMockDelivery(Long id, Long orderProductId, DeliveryStatus status) {
        return Delivery.builder()
                .id(id)
                .orderProductId(orderProductId)
                .status(status)
                .build();
    }

    public WishProduct newMockWishProduct(Long id, Long userId, Product product) {
        return WishProduct.builder()
                .id(id)
                .userId(userId)
                .product(product)
                .quantity(1)
                .price(product.getPrice())
                .build();
    }

}
