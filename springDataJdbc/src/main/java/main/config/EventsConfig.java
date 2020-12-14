package main.config;

import main.listeners.OwnerListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfig {

    @Bean
    public OwnerListener ownerListener() {
        return new OwnerListener();
    }
}
