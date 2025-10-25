package ru.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class Config {
    @Bean
    public RestClient dataSourceClient(@Value("${application.data-source}") String url) {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(url)
                .build();
    }

    @Bean(destroyMethod = "close")
    public ExecutorService executor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
