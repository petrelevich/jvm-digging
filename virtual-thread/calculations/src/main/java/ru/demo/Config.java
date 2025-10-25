package ru.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("java:S125")
@Configuration
public class Config {

    @Bean(name = "a1", destroyMethod = "close")
    public ExecutorService a1() {
        return Executors.newFixedThreadPool(1);
    }

    @Bean(name = "bti", destroyMethod = "close")
    public ExecutorService bti() {
        return Executors.newFixedThreadPool(1);
    }

    @Bean
    public ScheduledExecutorService scheduledExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
