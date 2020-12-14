package main.examples.repository.base;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import main.config.DataSourceConfig;
import main.config.FlywayConfig;
import main.examples.repository.SomeObjectRepository;

@TestConfiguration
@ImportAutoConfiguration(classes = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        JdbcRepositoriesAutoConfiguration.class,
        FlywayAutoConfiguration.class,
        JacksonAutoConfiguration.class
})
@Import({DataSourceConfig.class, FlywayConfig.class})
@ComponentScan(basePackageClasses = {SomeObjectRepository.class})
public class TestPersistenceConfiguration {
}
