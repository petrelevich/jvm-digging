package ru.demo.onetoone.jdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.demo.StringValue;

import java.util.concurrent.Executor;

public class StringValueProviderSeq implements StringValueProvider {
    private static final Logger log = LoggerFactory.getLogger(StringValueProviderSeq.class);
    private StringValueConsumer consumer;
    private final Executor executor;
    private final int end;
    private int current;


    public StringValueProviderSeq(int begin, int end, Executor executor) {
        this.executor = executor;
        this.end = end;
        this.current = begin;
    }

    @Override
    public void request(int n) {
        executor.execute(() -> {
            log.info(" new request on:{}", n);
            for (var idx = 0; (idx < n) && (current < end); idx++) {
                var stringValue = new StringValue(String.valueOf(current++));
                log.info(" StringValue:{}", stringValue);
                consumer.accept(stringValue);
            }
        });
    }

    @Override
    public void consume(StringValueConsumer consumer) {
        this.consumer = consumer;
    }
}
