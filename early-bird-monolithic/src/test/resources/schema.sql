CREATE TABLE `orders`
(
    `order_id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`            BIGINT UNSIGNED NOT NULL,
    `total_price`        BIGINT   NOT NULL,
    `created_date`       DATETIME NOT NULL,
    `last_modified_date` DATETIME NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE `deliveries`
(
    `delivery_id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_product_id`   BIGINT UNSIGNED NOT NULL,
    `delivery_status`    ENUM('READY_FOR_SHIPMENT', 'SHIPPED', 'DELIVERED', 'CANCELED') NOT NULL,
    `created_date`       DATETIME NOT NULL,
    `last_modified_date` DATETIME NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE `payments`
(
    `payment_id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`            BIGINT UNSIGNED NOT NULL,
    `order_id`           BIGINT UNSIGNED NOT NULL,
    `payment_status`     ENUM('NOT_PAY','SUCCESS', 'FAILED', 'CANCELLED') NOT NULL,
    `created_date`       DATETIME NOT NULL,
    `last_modified_date` DATETIME NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE `order_products`
(
    `order_product_id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `order_id`                 BIGINT UNSIGNED NOT NULL,
    `user_id`                  BIGINT UNSIGNED NOT NULL,
    `product_id`               BIGINT UNSIGNED NOT NULL,
    `order_status`             ENUM('CREATED', 'CANCELED') NOT NULL,
    `ordered_product_quantity` BIGINT   NOT NULL,
    `ordered_product_price`    BIGINT   NOT NULL,
    `created_date`             DATETIME NOT NULL,
    `last_modified_date`       DATETIME NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE
    `order_products`
    ADD CONSTRAINT `order_products_order_id_foreign`
        FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`);
CREATE TABLE `users`
(
    `user_id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_name`          VARCHAR(255) NOT NULL,
    `address`            VARCHAR(255) NOT NULL,
    `email`              VARCHAR(255) NOT NULL,
    `password`           VARCHAR(255) NOT NULL,
    `phone_number`       VARCHAR(255) NOT NULL,
    `role`               ENUM('ADMIN', 'USER') NOT NULL,
    `created_date`       DATETIME     NOT NULL,
    `last_modified_date` DATETIME     NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE
    `users`
    ADD UNIQUE `users_email_unique`(`email`);
CREATE TABLE `returns`
(
    `return_id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`            BIGINT UNSIGNED NOT NULL,
    `order_product_id`   BIGINT UNSIGNED NOT NULL,
    `return_reason`      VARCHAR(255) NOT NULL,
    `return_status`      ENUM('REQUESTED', 'APPROVED', 'DENIED') NOT NULL,
    `created_date`       DATETIME     NOT NULL,
    `last_modified_date` DATETIME     NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE
    `returns`
    ADD CONSTRAINT `returns_order_product_id_foreign` FOREIGN KEY (`order_product_id`) REFERENCES `order_products` (`order_product_id`);
CREATE TABLE `products`
(
    `product_id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`            BIGINT UNSIGNED NOT NULL,
    `product_name`       VARCHAR(50) NOT NULL,
    `category`           ENUM('FASHION', 'BEAUTY') NULL,
    `price`              BIGINT      NOT NULL,
    `description`        VARCHAR(255) NULL,
    `product_status`     ENUM('IN_STOCK', 'SOLD_OUT') NOT NULL,
    `quantity`           BIGINT      NOT NULL,
    `created_date`       DATETIME    NOT NULL,
    `last_modified_date` DATETIME    NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE `wish_products`
(
    `wish_product_id`       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id`               BIGINT UNSIGNED NOT NULL,
    `product_id`            BIGINT UNSIGNED NOT NULL,
    `wish_product_quantity` BIGINT   NOT NULL,
    `wish_product_price`    BIGINT   NOT NULL,
    `created_date`          DATETIME NOT NULL,
    `last_modified_date`    DATETIME NOT NULL
)DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE
    `wish_products`
    ADD CONSTRAINT `wish_products_product_id_foreign`
        FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);