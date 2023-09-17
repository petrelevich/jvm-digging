package main.config;

import main.listeners.ClientListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsConfig {

    @Bean
    public ClientListener ownerListener() {
        return new ClientListener();
    }
}
