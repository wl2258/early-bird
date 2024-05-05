package com.ssonzm.productservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@Slf4j
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestContainerConfig {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    @Container
    private static GenericContainer REDIS_CONTAINER = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
            .withExposedPorts(REDIS_PORT);

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