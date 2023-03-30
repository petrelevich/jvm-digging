package ru.demo.ehcache;


import org.ehcache.Cache;
import org.ehcache.CacheManager;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.SlowDataSrc;

import java.util.stream.IntStream;


public class EhcacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(EhcacheDemo.class);
    private final CacheManager cacheManager;
    private final Cache<Integer, Long> cache;

    public static void main(String[] args) {
        new EhcacheDemo().start();
    }

    public EhcacheDemo() {
        cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

        var cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(event ->
                                logger.info("updated key: {}, value: {}", event.getKey(), event.getNewValue()),
                        EventType.CREATED, EventType.UPDATED)
                .ordered().synchronous();

        cache = cacheManager.createCache("Demo-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Integer.class, Long.class,
                                ResourcePoolsBuilder.heap(5))
                        .withService(cacheEventListenerConfiguration)
                        .build());

        logger.info("Cache setup is done");
    }

    private void start() {
        logger.info("first getting...");
        IntStream.range(1, 10).forEach(val -> logger.info("value: {}", getValue(val)));
        logger.info("second getting...");
        IntStream.range(1, 10).map(i -> 10 - i).forEach(val -> logger.info("value: {}", getValue(val)));

        printAll();
        closeEhcache();
    }

    private long getValue(int key) {
        Long value = cache.get(key);
        if (value == null) {
            value = SlowDataSrc.getValue(key);
            cache.put(key, value);
        }
        return value;
    }

    private void printAll() {
        logger.info("content");
        for (Cache.Entry<Integer, Long> entry : cache) {
            var key = entry.getKey();
            var value = entry.getValue();
            logger.info("key:{}, value:{}", key, value);
        }
    }

    private void closeEhcache() {
        cacheManager.close();
    }
}
