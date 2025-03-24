package ru.demo;

import static java.time.Duration.ofSeconds;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.redisson.Bucket4jRedisson;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;
import java.time.Duration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

    private static final String LIMIT_KEY = "RL_REST_CLIENT";

    @Bean(destroyMethod = "shutdown")
    public RedissonClient commandExecutor(
            @Value("${application.redis.client-name}") String clientName,
            @Value("${application.redis.url}") String url,
            @Value("${application.redis.password}") String password) {
        log.info("client-name:{}, url:{}", clientName, url);
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
    public RedissonBasedProxyManager<String> proxyManager(RedissonClient redissonClient) {
        CommandAsyncExecutor commandExecutor = ((Redisson) redissonClient).getCommandExecutor();
        return Bucket4jRedisson.casBasedBuilder(commandExecutor)
                .expirationAfterWrite(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofMinutes(5)))
                .keyMapper(Mapper.STRING)
                .build();
    }

    @Bean
    public Bucket bucket(RedissonBasedProxyManager<String> proxyManager) {
        var bucketConfiguration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(10).refillGreedy(10, ofSeconds(60)))
                .build();
        return proxyManager.getProxy(LIMIT_KEY, () -> bucketConfiguration);
    }
}
