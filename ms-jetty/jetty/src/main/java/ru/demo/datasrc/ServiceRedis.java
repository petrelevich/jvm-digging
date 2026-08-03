package ru.demo.datasrc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRedis {
    private static final Logger log = LoggerFactory.getLogger(ServiceRedis.class);
    private final RedissonClient redissonClient;

    public ServiceRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void action(long id, Consumer<String> callback) {
        String key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        get(key, value -> {
            String result;
            if (value != null) {
                result = value;
            } else {
                result = "str:%s".formatted(id);
                put(key, result);
            }
            callback.accept(result);
        });
    }

    private void put(String key, String applValue) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.setAsync(applValue).thenAccept(value -> log.info("saved id:{}, value:{}", key, applValue));
    }

    private void get(String key, Consumer<String> valueConsumer) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        RFuture<String> bucketAsync = bucket.getAsync();
        bucketAsync
                .thenAccept(value -> {
                    log.info("key:{}, value:{}", key, value);
                    valueConsumer.accept(value);
                })
                .exceptionally(exception -> {
                    log.error("Error getting value for key:{}", key, exception);
                    return null;
                });
    }
}
