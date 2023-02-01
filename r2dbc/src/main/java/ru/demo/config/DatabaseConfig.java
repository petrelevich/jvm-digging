package ru.demo.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactoryOptions;
import java.time.Duration;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

@Configuration
public class DatabaseConfig {
    @Bean
    public Flyway flywayMigrations(DatabaseProperties databaseProperties) {
        return Flyway.configure()
                .dataSource(databaseProperties.urlJdbc(), databaseProperties.username(), databaseProperties.password())
                .locations("classpath:/db/migration")
                .load();
    }

    @Bean
    public PostgresqlConnectionFactory postgresqlConnectionFactory(DatabaseProperties databaseProperties) {
        var options = ConnectionFactoryOptions.parse(databaseProperties.urlR2dbc())
                .mutate()
                .option(USER, databaseProperties.username())
                .option(PASSWORD, databaseProperties.password())
                .build();

        var optionsPg = PostgresqlConnectionFactoryProvider.builder(options).build();
        return new PostgresqlConnectionFactory(optionsPg);
    }

    @Bean
    public ConnectionPool connectionFactory(PostgresqlConnectionFactory postgresqlConnectionFactory) {
        ConnectionPoolConfiguration poolConfig = ConnectionPoolConfiguration.builder()
                .connectionFactory(postgresqlConnectionFactory)
                .validationQuery("select 1")
                .maxIdleTime(Duration.ofSeconds(10))
                .backgroundEvictionInterval(Duration.ofSeconds(20))
                .maxCreateConnectionTime(Duration.ofSeconds(5))
                .maxAcquireTime(Duration.ofMinutes(10))
                .maxValidationTime(Duration.ofSeconds(5))
                .initialSize(5)
                .maxSize(10)
                .name("demoPool")
                .registerJmx(true)
                .build();

        return new ConnectionPool(poolConfig);
    }
}
