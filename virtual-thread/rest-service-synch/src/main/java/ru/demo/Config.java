package ru.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public AnyLiba anyLiba() {
        return new AnyLiba();
    }
}
