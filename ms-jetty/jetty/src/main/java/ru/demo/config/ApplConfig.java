package ru.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.datasrc.ServiceRedis;
import ru.demo.webserver.ApiHandler;
import ru.demo.webserver.ServerJetty;

@Configuration
public class ApplConfig {
    @Bean(destroyMethod = "stop")
    public ServerJetty serverJetty(@Value("${server.port}") int serverPort, ApiHandler apiHandler) {
        return new ServerJetty(serverPort, apiHandler);
    }

    @Bean
    public ApiHandler apiHandler(ServiceRedis serviceRedis) {
        return new ApiHandler(serviceRedis);
    }

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
    public ServiceRedis serviceRedis(RedissonClient redissonClient) {
        return new ServiceRedis(redissonClient);
    }
}
