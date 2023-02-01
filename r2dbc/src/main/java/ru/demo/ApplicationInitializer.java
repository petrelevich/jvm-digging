package ru.demo;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    private final Flyway flyway;

    public ApplicationInitializer(Flyway flyway) {
        this.flyway = flyway;
    }

    @Override
    public void run(String... args) {
        log.info("db migration started...");
        flyway.migrate();
        log.info("db migration finished.");
    }
}
