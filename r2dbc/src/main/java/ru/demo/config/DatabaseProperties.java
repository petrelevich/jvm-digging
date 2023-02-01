package ru.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database")
public record DatabaseProperties(
        String urlR2dbc,
        String urlJdbc,
        String username,
        String password
){}
