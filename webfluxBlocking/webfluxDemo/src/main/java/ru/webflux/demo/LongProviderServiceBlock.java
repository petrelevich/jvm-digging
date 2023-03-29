package ru.webflux.demo;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LongProviderServiceBlock implements LongProviderService {
    private static final Logger log = LoggerFactory.getLogger(LongProviderServiceBlock.class);
    @Override
    public long get(long value) {
        log.info("get request, value:{}", value);
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(value));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("get request, value:{}, done", value);
        return value;
    }
}
