package ru.demo.mainpackage.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAsync
@SuppressWarnings("java:S125")
public class ApplConfig {

    //    @Bean
    //    public UndertowDeploymentInfoCustomizer undertowDeploymentInfoCustomizer() {
    //        var factory = Thread.ofVirtual().name("undertow-", 0).factory();
    //        return deploymentInfo -> deploymentInfo.setExecutor(Executors.newThreadPerTaskExecutor(factory));
    //    }
}
