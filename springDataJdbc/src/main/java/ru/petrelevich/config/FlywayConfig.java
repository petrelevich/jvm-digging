package ru.petrelevich.config;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class FlywayConfig {
    private static final Logger log = LoggerFactory.getLogger(FlywayConfig.class);

    private final DataSource dataSource;

    public FlywayConfig(@Qualifier("demoDbDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    public void migrateFlyway() {
        log.info("Flyway migration started");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .load();
        flyway.migrate();
        log.info("Flyway migration ended");
    }
}
