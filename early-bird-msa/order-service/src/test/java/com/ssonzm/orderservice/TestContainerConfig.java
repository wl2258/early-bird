package com.ssonzm.orderservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Slf4j
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestContainerConfig {

    @Container
    static JdbcDatabaseContainer MARIADB_CONTAINER = (JdbcDatabaseContainer) new MariaDBContainer("mariadb:10.3.39")
            .withDatabaseName("early-bird")
            .withInitScript("schema.sql")
            .withCommand("--character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", () -> MARIADB_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> MARIADB_CONTAINER.getPassword());
    }

    @Test
    void test() {
        log.info(MARIADB_CONTAINER.getJdbcUrl());
        log.info(MARIADB_CONTAINER.getUsername());
        log.info(MARIADB_CONTAINER.getPassword());
    }
}