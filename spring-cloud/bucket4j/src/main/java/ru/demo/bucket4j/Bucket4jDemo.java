package ru.demo.bucket4j;

import static java.time.Duration.ofSeconds;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketListener;
import io.github.bucket4j.VerboseResult;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bucket4jDemo {
    private static final Logger log = LoggerFactory.getLogger(Bucket4jDemo.class);

    public static void main(String[] args) {
        new Bucket4jDemo().demo();
    }

    private void demo() {
        var consumer = new Consumer();

        BucketListener bucketListenerLogger = new BucketListenerLogger();
        Bucket bucket = Bucket.builder()
                .addLimit(limit -> limit.capacity(10).refillGreedy(10, ofSeconds(60)))
                // .addLimit(limit -> limit.capacity(10).refillIntervally(5, ofSeconds(30)))
                .withListener(bucketListenerLogger)
                .build();

        while (!Thread.currentThread().isInterrupted()) {
            List<Message> messages = consumer.poll();
            for (Message message : messages) {
                var verboseBucket = bucket.asVerbose();

                VerboseResult<Boolean> consumeResult = verboseBucket.tryConsume(1);
                var availableTokens = consumeResult.getState().getAvailableTokens();
                boolean consumeResultValue = consumeResult.getValue();
                log.info("consumeResultValue:{}, availableTokens:{}", consumeResultValue, availableTokens);
                if (consumeResultValue) {
                    process(message);
                } else {
                    sleep();
                }
            }
        }
    }

    private void process(Message message) {
        log.info("processing message:{}", message);
        sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(Duration.ofSeconds(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
