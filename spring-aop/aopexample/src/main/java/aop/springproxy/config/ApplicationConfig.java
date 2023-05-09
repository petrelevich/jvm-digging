package aop.springproxy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    // ConfigurationClassEnhancer

    @Bean
    public DemoBeanArgument demoBeanArgument() {
        return new DemoBeanArgument();
    }

    @Bean
    public DemoBean demoBean1() {
        return new DemoBean(demoBeanArgument());
    }

    @Bean
    public DemoBean demoBean2() {
        return new DemoBean(demoBeanArgument());
    }
}
