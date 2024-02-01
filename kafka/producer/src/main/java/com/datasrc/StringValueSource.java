package com.datasrc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class StringValueSource implements ValueSource, AutoCloseable {
    private final AtomicLong nextValue = new AtomicLong(1);

    private final Consumer<StringValue> valueConsumer;
    private final ScheduledExecutorService executor;

    public StringValueSource(Consumer<StringValue> valueConsumer) {
        this.valueConsumer = valueConsumer;
        executor = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void generate() {
        executor.scheduleAtFixedRate(() -> valueConsumer.accept(makeValue()), 0, 1, TimeUnit.SECONDS);
    }

    private StringValue makeValue() {
        var id = nextValue.getAndIncrement();
        return new StringValue(id, "stVal:" + id);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
