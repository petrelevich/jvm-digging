package ru.demo.mainpackage.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.demo.mainpackage.model.StatsValue;
import ru.demo.mainpackage.repository.CounterRepository;
import ru.demo.mainpackage.repository.CounterRepositoryRedis;
import ru.demo.mainpackage.services.CounterRedis;

@Configuration
public class ApplConfig {
    private static final Logger log = LoggerFactory.getLogger(ApplConfig.class);

    @Bean(destroyMethod = "shutdownNow")
    public ScheduledThreadPoolExecutor scheduledExecutor() {
        return new ScheduledThreadPoolExecutor(
                2, task -> Thread.ofVirtual().name("scheduler").unstarted(task));
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(
            @Value("${application.client-name}") String clientName,
            @Value("${application.redis.url}") String url,
            @Value("${application.redis.password}") String password) {
        log.info("redis.url:{}, clientName:{}", url, clientName);
        var config = new Config();
        config.useSingleServer()
                .setAddress(String.format("redis://%s", url))
                .setPassword(password)
                .setClientName(clientName)
                .setConnectionMinimumIdleSize(1)
                .setConnectionPoolSize(1);
        return Redisson.create(config);
    }

    @Bean
    public CounterRepository counterRepository(RedissonClient redissonClient) {
        return new CounterRepositoryRedis(redissonClient);
    }

    @Bean
    public CounterRedis counterRedis(
            CounterRepository counterRepository,
            ScheduledThreadPoolExecutor scheduledExecutor,
            @Value("${application.init-value}") long initValue) {
        log.info("initValue:{}", initValue);
        return new CounterRedis(counterRepository, scheduledExecutor, new StatsValue(initValue));
    }
}
