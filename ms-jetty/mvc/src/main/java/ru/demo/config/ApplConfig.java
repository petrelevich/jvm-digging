package ru.demo.config;

import java.net.http.HttpClient;
import java.time.Duration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.datasrc.ServiceRedis;

@Configuration
public class ApplConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(
            @Value("${application.client-name}") String clientName,
            @Value("${application.redis.url}") String url,
            @Value("${application.redis.password}") String password) {
        var config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s", url))
                .setClientName(clientName)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(5);
        config = config.setPassword(password);
        return Redisson.create(config);
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofMillis(500))
                .build();
    }

    @Bean
    public ServiceRedis serviceRedis(RedissonClient redissonClient) {
        return new ServiceRedis(redissonClient);
    }
}
