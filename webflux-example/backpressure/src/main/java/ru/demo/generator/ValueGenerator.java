package ru.demo.generator;

import java.time.Duration;

public class ValueGenerator {
    private final Subscriber subscriber;
    private final int end;
    private int current;

    public ValueGenerator(Subscriber subscriber, int begin, int end) {
        this.subscriber = subscriber;
        this.current = begin;
        this.end = end;
    }

    public void request(int n) {
        for (var idx = 0; idx < n; idx++) {
            if (current < end) {
                var val = generate(current++);
                subscriber.onNext(val);
            } else {
                subscriber.onComplete();
                return;
            }
        }
    }


    private Value generate(int val) {
        try {
            Thread.sleep(Duration.ofSeconds(3).toMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return new Value(val);
    }
}
