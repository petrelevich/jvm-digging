package io.squad.apisec;

import io.squad.apisec.controller.InstanceId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    InstanceId serviceName(@Value("${spring.application.instance_id}") String id) {
        return new InstanceId(id);
    }
}
