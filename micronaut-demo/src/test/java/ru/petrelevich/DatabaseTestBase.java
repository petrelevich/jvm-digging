package ru.petrelevich;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DatabaseTestBase implements TestPropertyProvider {
    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>("postgres:16");

    static {
        POSTGRE_SQL_CONTAINER.start();
    }

    public static PostgreSQLContainer<?> getPostgreSqlContainer() {
        return POSTGRE_SQL_CONTAINER;
    }

    @Override
    public @NonNull Map<String, String> getProperties() {
        var container = DatabaseTestBase.getPostgreSqlContainer();
        return Map.of(
                "datasources.demoDB.url", String.format("jdbc:postgresql://%s:%d/%s",
                        container.getHost(), container.getFirstMappedPort(), container.getDatabaseName()),
                "datasources.demoDB.username", container.getUsername(),
                "datasources.demoDB.password", container.getPassword(),

                "r2dbc.datasources.demoDBr.url", String.format("r2dbc:postgresql://%s:%d/%s",
                        container.getHost(), container.getFirstMappedPort(), container.getDatabaseName()),
                "r2dbc.datasources.demoDBr.username", container.getUsername(),
                "r2dbc.datasources.demoDBr.password", container.getPassword()
        );
    }
}
