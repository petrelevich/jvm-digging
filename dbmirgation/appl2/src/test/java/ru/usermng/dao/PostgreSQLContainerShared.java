package ru.usermng.dao;

import org.testcontainers.containers.PostgreSQLContainer;
import ru.usermng.ApplicationV2;

public class PostgreSQLContainerShared extends PostgreSQLContainer<PostgreSQLContainerShared> {
    private static final String IMAGE_VERSION = "postgres:12";
    private static PostgreSQLContainerShared container;

    private PostgreSQLContainerShared() {
        super(IMAGE_VERSION);
    }

    public static PostgreSQLContainerShared getInstance() {
        if (container == null) {
            container = new PostgreSQLContainerShared();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        ApplicationV2.flywayMigrations(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    @Override
    public void stop() {
    }
}