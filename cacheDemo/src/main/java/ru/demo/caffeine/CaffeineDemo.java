package ru.demo.caffeine;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import java.time.Duration;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.SlowDataSrc;

public class CaffeineDemo {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineDemo.class);
    private final Cache<Integer, Long> cache;

    public static void main(String[] args) {
        new CaffeineDemo().start();
    }

    public CaffeineDemo() {
        cache = Caffeine.newBuilder()
                .maximumSize(5)
                .expireAfterWrite(Duration.ofMinutes(60))
                .removalListener((Integer key, Long value, RemovalCause cause) ->
                        logger.info("Key:{} was removed, value:{}, cause:{}", key, value, cause))
                .build();
        logger.info("Cache setup is done");
    }

    private void start() {
        logger.info("first getting...");
        IntStream.range(1, 10).forEach(val -> logger.info("value: {}", this.getValue(val)));
        logger.info("second getting...");
        IntStream.range(1, 10).map(i -> 10 - i).forEach(val -> logger.info("value: {}", this.getValue(val)));

        printAll();
    }

    private void printAll() {
        logger.info("content");
        for(var entry: cache.asMap().entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();
            logger.info("key:{}, value:{}", key, value);
        }
    }

    private long getValue(int key) {
        return cache.get(key, SlowDataSrc::getValue);
    }
}
