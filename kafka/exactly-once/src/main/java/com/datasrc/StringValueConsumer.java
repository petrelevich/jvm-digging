package com.datasrc;

import static com.datasrc.config.ConsumerManualCommit.MAX_POLL_INTERVAL_MS;

import com.datasrc.config.ConsumerManualCommit;
import com.datasrc.deduplicator.DeDuplicator;
import com.datasrc.deduplicator.StringValueProcessor;
import com.datasrc.model.StringValue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringValueConsumer {
    private static final Logger log = LoggerFactory.getLogger(StringValueConsumer.class);

    private final ConsumerManualCommit consumerManualCommit;
    private final Duration timeout = Duration.ofMillis(2_000);
    private final StringValueProcessor dataConsumer;
    private final DeDuplicator deDuplicator;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public StringValueConsumer(
            ConsumerManualCommit consumerManualCommit, DeDuplicator deDuplicator, StringValueProcessor dataConsumer) {
        this.consumerManualCommit = consumerManualCommit;
        this.dataConsumer = dataConsumer;
        this.deDuplicator = deDuplicator;
    }

    public void startSending() {
        executor.scheduleAtFixedRate(
                this::poll, 0, MAX_POLL_INTERVAL_MS, TimeUnit.MILLISECONDS); // try MAX_POLL_INTERVAL_MS * 2L
    }

    private void poll() {
        log.info("poll records");
        var consumer = consumerManualCommit.getConsumer();
        var records = consumer.poll(timeout);
        if (records.isEmpty()) {
            return;
        }

        sleep();
        log.info("polled records.counter:{}", records.count());
        var notCommittedValues = new ArrayList<StringValue>();
        try {
            for (var kafkaRecord : records) {
                var key = kafkaRecord.key();
                var value = kafkaRecord.value();
                notCommittedValues.add(value);
                log.info("key:{}, value:{}, record:{}", key, value, kafkaRecord);
                if (!deDuplicator.process(value, dataConsumer)) {
                    log.info("key:{}, value:{}. Already processed", key, value);
                }
            }
            // случай 2. Ошибка до commit в Kafka
            System.exit(-2);
            consumer.commitSync(Duration.ofMillis(500));
            log.info("commit done");
        } catch (Exception ex) {
            log.error("Error, notCommittedValues:{}, offsetLast:{}", notCommittedValues, ex);
        }
    }

    public void stopPulling() {
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
