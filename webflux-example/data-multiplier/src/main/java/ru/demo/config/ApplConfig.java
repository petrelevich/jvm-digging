package ru.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplConfig {

    @Bean
    public ApplParams applParams(
            @Value("${application.delay-sec}") int delaySec,
            @Value("${application.multiplier}") int multiplier) {
        return new ApplParams(delaySec, multiplier);
    }
}
