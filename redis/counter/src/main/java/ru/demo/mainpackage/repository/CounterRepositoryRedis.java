package ru.demo.mainpackage.repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.mainpackage.model.Stats;
import ru.demo.mainpackage.model.StatsId;
import ru.demo.mainpackage.model.StatsValue;

public class CounterRepositoryRedis implements CounterRepository {
    private static final Logger log = LoggerFactory.getLogger(CounterRepositoryRedis.class);
    private final RedissonClient redissonClient;

    public CounterRepositoryRedis(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    @SuppressWarnings("java:S2142")
    public void initValue(Stats stats) {
        var lockKey = String.format("REDIS_LOCK:%d", stats.id().data());

        log.info("init value:{}, get lock, key:{}", stats, lockKey);
        var lock = redissonClient.getLock(lockKey);
        boolean initResult;
        try {
            log.info("init value:{}, try lock", stats);
            initResult = lock.tryLock(100, 2, TimeUnit.SECONDS);
            if (initResult) {
                try {
                    log.info("init value:{}, locked", stats);
                    var counter = redissonClient.getAtomicLong(getKeyName(stats.id()));
                    if (counter.isExists()) {
                        initResult = false;
                    } else {
                        counter.set(stats.value().data());
                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException ie) {
            initResult = false;
        }
        if (initResult) {
            log.info("init value:{}, done", stats);
        } else {
            log.info("init value:{}, already set", stats);
        }
    }

    @Override
    public Optional<StatsValue> get(StatsId id) {
        var counter = redissonClient.getAtomicLong(getKeyName(id));
        if (counter == null) {
            return Optional.empty();
        }
        return Optional.of(new StatsValue(counter.get()));
    }

    @Override
    public StatsValue increment(StatsId id) {
        var counter = redissonClient.getAtomicLong(getKeyName(id));
        var lastValue = counter.incrementAndGet();
        return new StatsValue(lastValue);
    }

    private String getKeyName(StatsId id) {
        return String.valueOf(id.data());
    }
}
