package com.datasrc;

import static com.datasrc.MyConsumer.MAX_POLL_INTERVAL_MS;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringValueConsumer {
    private static final Logger log = LoggerFactory.getLogger(StringValueConsumer.class);

    private final MyConsumer myConsumer;
    private final Duration timeout = Duration.ofMillis(2_000);
    private final Consumer<StringValue> dataConsumer;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public StringValueConsumer(MyConsumer myConsumer, Consumer<StringValue> dataConsumer) {
        this.myConsumer = myConsumer;
        this.dataConsumer = dataConsumer;
    }

    public void startSending() {
        executor.scheduleAtFixedRate(
                this::poll,
                0,
                MAX_POLL_INTERVAL_MS,
                TimeUnit.MILLISECONDS); // try MAX_POLL_INTERVAL_MS * 2L
    }

    private void poll() {
        log.info("poll records");
        ConsumerRecords<Long, StringValue> records = myConsumer.getConsumer().poll(timeout);
        sleep();
        log.info("polled records.counter:{}", records.count());
        for (ConsumerRecord<Long, StringValue> kafkaRecord : records) {
            try {
                var key = kafkaRecord.key();
                var value = kafkaRecord.value();
                log.info("key:{}, value:{}, record:{}", key, value, kafkaRecord);
                dataConsumer.accept(value);
            } catch (Exception ex) {
                log.error("can't parse record:{}", kafkaRecord, ex);
            }
        }
    }

    public void stopSending() {
        executor.shutdown();
    }

    private void sleep() {
        try {
            Thread.sleep(1); // try 500
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
