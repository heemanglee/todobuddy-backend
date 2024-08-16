package com.todobuddy.backend;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class TestContainerSupport {

    @Container
    static MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.33")
        .withDatabaseName("test");

    static {
        MYSQL_CONTAINER.start();
    }
}
