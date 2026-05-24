package ru.demo.datasrc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRedis {
    private static final Logger log = LoggerFactory.getLogger(ServiceRedis.class);

    private final RedissonClient redissonClient;

    public ServiceRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public String action(long id) {
        String key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        var value = get(key);
        if (value == null) {
            value = "str:%s".formatted(id);
            put(key, value);
        }
        return value;
    }

    private String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        var value = bucket.get();
        log.info("key:{}, value:{}", key, value);
        return value;
    }

    private void put(String key, String applValue) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(applValue);
        log.info("saved id:{}, value:{}", key, applValue);
    }
}
