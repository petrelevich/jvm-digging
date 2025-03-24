package ru.demo.bucket4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class Consumer {
    private final AtomicLong id = new AtomicLong(0);
    private static final int POLL_SIZE = 100;

    public List<Message> poll() {
        return IntStream.range(0, POLL_SIZE)
                .mapToObj(val -> new Message(String.format("val:%d", id.incrementAndGet())))
                .toList();
    }
}
